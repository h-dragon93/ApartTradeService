package com.estate.hdragon.apart.job;

import com.estate.hdragon.apart.api.ApartApiService;
import com.estate.hdragon.apart.data.AptTradeData;
import com.estate.hdragon.apart.data.AptTradeRedisData;
import com.estate.hdragon.apart.data.Lawdcd;
import com.estate.hdragon.apart.repository.ApartTradeRedisRepository;
import com.estate.hdragon.apart.repository.ApartTradeRepository;
import com.estate.hdragon.apart.util.JsonObjectUtil;
import com.estate.hdragon.apart.writer.JpaitemListWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApartJobConfiguration {
    public static final String JOB_NAME = "apartBatchJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ApartApiService api;
    private final ApartTradeRedisRepository apartTradeRedisRepository;

//    @Autowired
//    private ApartTradeRepository apartTradeRepository;

    private int chunkSize = 10;

    @Bean
    public Job apartJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(apartStep1())
                .next(apartStep2())
                .build();
    }

    @Bean
    @JobScope
    public Step apartStep1() {
        return stepBuilderFactory.get("apartStep1")
                .<Lawdcd, StringBuilder>chunk(chunkSize)  //<Reader에서 반환할 타입, Writer에 파라미터로 넘어 올 타입>
                .reader(jpaPagingItemReader())
                .processor(compositeItemProcessor())
                .writer(multiEntityJpaWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Lawdcd> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Lawdcd>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select l from Lawdcd l order by lawd_cd")  // 쿼리 수정
                .build();
    }

    @Bean                       // Marking a @Bean as @StepScope is
    @StepScope                  // equivalent to marking it as @Scope(value="step", proxyMode=TARGET_CLASS
    public CompositeItemProcessor compositeItemProcessor() {
        List<ItemProcessor> delegates = new ArrayList<>(2);
        delegates.add(processor1());
        delegates.add(processor2());
        delegates.add(processor3());

        CompositeItemProcessor processor = new CompositeItemProcessor<>();
        processor.setDelegates(delegates);
        return processor;
    }

    @Bean
    @StepScope
    public ItemProcessor<Lawdcd, StringBuilder> processor1() {
        return item -> {
            StringBuilder tradeInfo = new StringBuilder();
            try {
                tradeInfo  = api.callApartInfo(item.getLawd_cd(),"202304");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return tradeInfo;
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<StringBuilder, ArrayList<AptTradeData>> processor2() {
        return item -> {

            JSONObject xmlJSONObj = XML.toJSONObject(item.toString());
            String resultCode = xmlJSONObj.getJSONObject("response").getJSONObject("header").getString("resultCode");
            int totalCount = xmlJSONObj.getJSONObject("response").getJSONObject("body").getInt("totalCount");
            if ("00".equals(resultCode) && totalCount != 0) {
                List<JSONObject> JsonItemList = new ArrayList<>();
                if ( totalCount == 1 ) {
                    JSONObject JsonItemObject = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONObject("item");
                    JsonItemList.add(JsonItemObject);
                } else {
                    JSONArray JsonItemArray = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
                    JsonItemList = IntStream.range(0, JsonItemArray.length())
                                            .mapToObj(index -> (JSONObject) JsonItemArray.get(index))
                                            .collect(Collectors.toList());
                }
                ArrayList<AptTradeData> AptTradeList = itemProcessorForRedis(JsonItemList);

                return AptTradeList;
            }
            return null;
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<ArrayList<AptTradeData>, ArrayList<AptTradeData>> processor3() {
        return item -> {

            // apartTradeRedisRepository.findById()
            // 리스트를 순회하며 거래 데이터를 redis에서 조회 해오고, redis에 없으면 신규 리스트에 담아 db와 redis에 넣자
            // redis에 데이터가 있다면 cancle_yn을 비교한다
            // redis에 데이터가 있고 cancle_yn 이 다르다면 취소 여부 확인 후 db에 upsert 해준다
            // redis에 올라가있는 데이터는 이미 db에 한달 이내에 담긴 데이터 이므로 버린다
            // 조회 기간이 한달이면 API 기준으로 이 전 달 까지 2번은 나가야 한달치 거래데이터가 처리 가능하다..
            return item;
        };
    }


                                        // JpaItemWriter는 엔티티 객체를 제네릭하게 받아줘야 write이 가능한데,
                                        // JpaitemListWriter 를 구현체 return type으로 사용하지 않으면 프록시 객체로 인해 엔티티매니저가 생성되지 못함
                                        // Bean과 StepCope 어노테이션을 지웠더니 entitymanager factory create 오류 떨어진게 정상적으로 insert 됐음.... 뭐지?
    public JpaitemListWriter<AptTradeData> multiEntityJpaWriter() {        // itemWriter 인터페이스는 write 메소드의 인자로 List를 받는다 따라서

        JpaItemWriter<AptTradeData> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
                                                                                // processor 로 부터 전달받은 ArrayList는 List 안에 들어간 채 반환된다
        return new JpaitemListWriter(jpaItemWriter);
    }

    @Bean
    @JobScope
    public Step apartStep2() {
        return stepBuilderFactory.get("apartStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(contribution);
                    log.info("### TEST in apartStep2 ###");
                    System.out.println(chunkContext);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private ArrayList<AptTradeData> itemProcessorForRedis(List<JSONObject> JsonItemList) {

        ArrayList<AptTradeData> AptTradeList = new ArrayList<AptTradeData>();

        JsonItemList.forEach(arrayElement -> {
            JSONObject clonedObject = new JSONObject(arrayElement, JSONObject.getNames(arrayElement));
            clonedObject = JsonObjectUtil.getRedisJsonObject(clonedObject);

            arrayElement =  JsonObjectUtil.getTranslatedJsonObject(arrayElement);
            String jsonString = arrayElement.toString();

            ObjectMapper mapper = new ObjectMapper();
            AptTradeData AptTradeDataJson = null;

            try {
                AptTradeDataJson = mapper.readValue(jsonString, AptTradeData.class);
                AptTradeRedisData AptTradeRedisDataJson = mapper.readValue(clonedObject.toString(),AptTradeRedisData.class);
                apartTradeRedisRepository.save(AptTradeRedisDataJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            AptTradeList.add(AptTradeDataJson);
        });

        return AptTradeList;
    }

}
