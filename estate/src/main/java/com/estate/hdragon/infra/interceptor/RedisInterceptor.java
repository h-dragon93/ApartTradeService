package com.estate.hdragon.infra.interceptor;

import com.estate.hdragon.domain.account.AccountProvider;
import com.estate.hdragon.domain.account.kakao.KakaoAccessToken;
import com.estate.hdragon.domain.account.kakao.KakaoProfile;
import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshToken;
import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshTokenRedisRepository;
import com.estate.hdragon.infra.common.CommonConfig;
import com.estate.hdragon.infra.common.CryptoInfo;
import com.estate.hdragon.infra.util.AESCryptoUtil;
import com.estate.hdragon.infra.util.CookieUtil;
import com.estate.hdragon.infra.util.HttpSessionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RedisInterceptor implements HandlerInterceptor {

    Logger Logger = LoggerFactory.getLogger(RedisInterceptor.class);
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final AccountProvider accountProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String idAccessToken = CookieUtil.getAccessTokenFromCookie(request);
        if(idAccessToken != null && !idAccessToken.isEmpty()) {         // accessToken 쿠키가 있으면
            String decryptedText = AESCryptoUtil.decrypt(AESCryptoUtil.SPEC_NAME, CryptoInfo.getInstance().getKey(), CryptoInfo.getInstance().getIvParameterSpec(), idAccessToken);
            String[] decrypted = decryptedText.split("\\|");
            String kakaoUniqueId = decrypted[0];
            String accessToken = decrypted[1];
            KakaoAccessToken kakaoAccessToken = getKakaoAccessTokenInfo(accessToken);

            if(kakaoAccessToken.getHttpStatusCode() == HttpStatus.UNAUTHORIZED.value()) { // kakao access token 만료
                System.out.println("액세스 토큰 만료 확인"); // 액세스 토큰 만료지만 리프레시 토큰 살아있는 경우 액세스 토큰 재발급
            }

        } else {        // accessToken 쿠키가 없으면 Redis에서 refreshToken 검증

        }

        System.out.println("Not Logined");
        return true;
    }

    private KakaoAccessToken getKakaoAccessTokenInfo(String accessToken) throws JsonProcessingException {
        KakaoAccessToken kakaoAccessToken = accountProvider.getKakaoAccessTokenInfo(accessToken);
        return kakaoAccessToken;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
