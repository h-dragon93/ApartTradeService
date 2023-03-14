package com.estate.hdragon.apart.processor;

import com.estate.hdragon.apart.api.ApartApiService;
import com.estate.hdragon.apart.data.Lawdcd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;


// ## 삭제 예정 클래스
@Slf4j
//@RequiredArgsConstructor
public class ItemApartProcessor implements ItemProcessor {

    //private final ApartApiService api;

    public ItemApartProcessor() {

    }

    @Override
    public Object process(Object item) throws Exception {
        System.out.println();
        System.out.println("in ItemApartProcessor");
        log.info(String.valueOf((Lawdcd) item));
        return null;
    }
}

