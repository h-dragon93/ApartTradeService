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
    private String redirectUrl;    // http://localhost:8087/login/oauth/kakao
    private String oauthTokenUrl;  // https://kauth.kakao.com/oauth/token
    private String userMe;         // https://kapi.kakao.com/v2/user/me
    private String userAccessTokenInfo;  // https://kapi.kakao.com/v1/user/access_token_info
}
