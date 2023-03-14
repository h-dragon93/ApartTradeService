package com.estate.hdragon.apart.repository;

import com.estate.hdragon.apart.data.AptTradeRedisData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class ApartTradeRedisRepository {

    private RedisTemplate redisTemplate;

    public ApartTradeRedisRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final AptTradeRedisData aptTradeRedisData) {
        ValueOperations<String, AptTradeRedisData> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(aptTradeRedisData.getTradeId(), aptTradeRedisData);
        redisTemplate.expire(aptTradeRedisData.getTradeId(), 600L, TimeUnit.SECONDS);
    }

    public Optional<AptTradeRedisData> findById(final String tradeId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String dealAmount = valueOperations.get(tradeId);

        if (Objects.isNull(tradeId)) {
            return Optional.empty();
        }

        return Optional.of(new AptTradeRedisData(tradeId, dealAmount));
    }
}