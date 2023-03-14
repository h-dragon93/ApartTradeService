package com.estate.hdragon.apart.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;



@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "aptTradeRedis")
public class AptTradeRedisData {

    @NonNull
    @Id
    private String tradeId; // lawd_cd + jibun + month+ day + floor
    @NonNull
    private String dealAmount;
    private String cancelYn;
    private String cancelDealDay;
}
