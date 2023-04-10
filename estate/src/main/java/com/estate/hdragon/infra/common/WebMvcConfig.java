package com.estate.hdragon.infra.common;

import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshTokenRedisRepository;
import com.estate.hdragon.infra.interceptor.RedisInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

private final RefreshTokenRedisRepository refreshTokenRedisRepository;

@Override
public void addInterceptors(InterceptorRegistry registry) {

        registry
                .addInterceptor(new RedisInterceptor(refreshTokenRedisRepository))
                .addPathPatterns("/*");
}

}
