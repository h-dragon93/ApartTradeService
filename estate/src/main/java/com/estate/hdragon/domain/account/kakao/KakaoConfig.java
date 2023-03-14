package com.estate.hdragon.domain.account.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ConfigurationProperties("kakao")
@Configuration
@RefreshScope
@ToString
public class KakaoConfig {

    private String authUrl;
    private String uri;
    private String apiKey;
    private String redirectUrl;
    private String oauthTokenUrl;
    private String userMe;
}
