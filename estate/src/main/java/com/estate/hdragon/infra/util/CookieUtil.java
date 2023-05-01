package com.estate.hdragon.infra.util;

import com.estate.hdragon.infra.common.CryptoInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static String getAccessTokenFromCookie(HttpServletRequest request) {
        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if ("accessCookie".equals(cookies[i].getName())) {
                    accessToken = cookies[i].getValue();
                }
            }
        }
        return accessToken;
    }

    public static Cookie makeKakaoAccessCookie(Cookie accessCookie, Long kakaoUniqueId, String accessToken) throws Exception {

        String cookieValue = String.valueOf(kakaoUniqueId)+"|"+accessToken;
        String encryptedText = AESCryptoUtil.encrypt(AESCryptoUtil.SPEC_NAME, CryptoInfo.getInstance().getKey(), CryptoInfo.getInstance().getIvParameterSpec(), cookieValue);

        accessCookie.setValue(encryptedText);
        accessCookie.setMaxAge(600);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");

        return accessCookie;
    }

}
