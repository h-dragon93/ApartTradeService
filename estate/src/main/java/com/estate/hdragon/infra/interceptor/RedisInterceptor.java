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
        if(idAccessToken != null && !idAccessToken.isEmpty()) {
            String decryptedText = AESCryptoUtil.decrypt(AESCryptoUtil.SPEC_NAME, CryptoInfo.getInstance().getKey(), CryptoInfo.getInstance().getIvParameterSpec(), idAccessToken);
            System.out.println("decryptedText : " + decryptedText);
            String[] decrypted = decryptedText.split("\\|");
            String kakaoUniqueId = decrypted[0];
            String accessToken = decrypted[1];
            System.out.println("kakaoUniqueId : " + kakaoUniqueId + "accessToken : " + accessToken);
            KakaoAccessToken kakaoAccessToken = getKakaoAccessTokenInfo(accessToken);
            System.out.println("kakaoAccessToken getId : " + kakaoAccessToken.getId());
            System.out.println("kakaoAccessToken getApp_id : " + kakaoAccessToken.getApp_id());
            System.out.println("kakaoAccessToken getExpires_in : " + kakaoAccessToken.getExpires_in());

        }

        if (HttpSessionUtil.isLogin(HttpSessionUtil.getSession(request))) {
            String redisId = (String) HttpSessionUtil.getSession(request).getAttribute(CommonConfig.USER_SESSION_ID);
            Optional<RefreshToken> refreshToken = refreshTokenRedisRepository.findById((String) HttpSessionUtil.getSession(request).getAttribute(CommonConfig.USER_SESSION_ID));
            return true;
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
