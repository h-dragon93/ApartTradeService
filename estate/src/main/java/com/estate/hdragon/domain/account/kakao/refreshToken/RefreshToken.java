package com.estate.hdragon.domain.account.kakao.refreshToken;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;




// Redis 저장을 위한 객체
//@RedisHash(value = "refreshToken", timeToLive = 600) jpa repository 사용시 사용
@ToString
public class RefreshToken {

    @Id
    private String refreshToken;

    private Long memberId;

    public RefreshToken(final String refreshToken, final Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getMemberId() {
        return memberId;
    }
}