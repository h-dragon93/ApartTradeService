package com.estate.hdragon.infra.interceptor;

import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshToken;
import com.estate.hdragon.domain.account.kakao.refreshToken.RefreshTokenRedisRepository;
import com.estate.hdragon.infra.common.CommonConfig;
import com.estate.hdragon.infra.session.HttpSessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class RedisInterceptor implements HandlerInterceptor {

    Logger Logger = LoggerFactory.getLogger(RedisInterceptor.class);
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpSessionUtil.isLogin(HttpSessionUtil.getSession(request))) {
            Optional<RefreshToken> refreshToken = refreshTokenRedisRepository.findById((String) HttpSessionUtil.getSession(request).getAttribute(CommonConfig.USER_SESSION_ID));
            System.out.println("refreshToken in preHandle: " + refreshToken);
            return true;
        }
        System.out.println("Not Logined");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        //Logger.info("postHandle 1");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //Logger.info("afterCompletion 1");
    }
}
