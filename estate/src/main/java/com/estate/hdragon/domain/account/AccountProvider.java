package com.estate.hdragon.domain.account;

import com.estate.hdragon.domain.account.kakao.KakaoConfig;
import com.estate.hdragon.domain.account.kakao.KakaoProfile;
import com.estate.hdragon.domain.account.kakao.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class AccountProvider {

    @Value("${kakao.apiKey}")
    private String API_KEY;

    private final KakaoConfig kakaoConfig;

    // 카카오 로그인 access_token 리턴
    public KakaoToken getKakaoToken(HttpServletRequest request, String code) throws Exception {

        //try {
        RestTemplate restTemplate = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", API_KEY);
        params.add("redirect_uri", kakaoConfig.getRedirectUrl());
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Http Post
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoConfig.getOauthTokenUrl(),
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken oauthTokenKakao = objectMapper.readValue(response.getBody(), KakaoToken.class);

        return oauthTokenKakao;
    }


    public KakaoProfile getKakaoProfile(String accessToken){

        RestTemplate rt = new RestTemplate();
        //HttpHeader
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + accessToken);
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader and HttpBody
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        //  Http - Post -> response
        ResponseEntity<String> response2 = rt.exchange(
                kakaoConfig.getUserMe(),
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        // Json Mapping
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
}
