package com.estate.hdragon.domain.account.kakao.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


// Redis 저장을 위한 객체
//@RedisHash(timeToLive = 1800L) //jpa repository 사용시 사용
@ToString
@Getter
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String memberId;
    private String refreshToken;

}