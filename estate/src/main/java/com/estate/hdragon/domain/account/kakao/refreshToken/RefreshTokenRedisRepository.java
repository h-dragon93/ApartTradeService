package com.estate.hdragon.domain.account.kakao.refreshToken;

import com.estate.hdragon.domain.account.kakao.KakaoAccessToken;
import com.estate.hdragon.domain.account.kakao.KakaoToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRedisRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRedisRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final KakaoToken kakaoToken, Long kakaoUid) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("accessToken:"+kakaoUid, kakaoToken.getAccess_token());
        redisTemplate.expire("accessToken:"+kakaoUid, 40L, TimeUnit.SECONDS);
    }

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("refreshToken:"+refreshToken.getMemberId(), refreshToken.getRefreshToken());
        redisTemplate.expire("refreshToken:"+refreshToken.getMemberId(), 80L, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String memberId = String.valueOf(valueOperations.get(refreshToken));

        if (Objects.isNull(memberId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(memberId, refreshToken));
    }
}