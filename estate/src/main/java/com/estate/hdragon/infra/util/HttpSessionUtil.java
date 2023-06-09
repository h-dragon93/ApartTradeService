package com.estate.hdragon.infra.util;

import com.estate.hdragon.infra.common.CommonConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpSessionUtil {

    public static boolean isLogin(HttpSession session) {
        Object sessionUser = session.getAttribute(CommonConfig.USER_SESSION_ID);
        if (sessionUser == null) {
            return false;
        }
        return true;
    }

    public static String getSessionIdFromSession(HttpSession session) {
        if(!isLogin(session)) {
            return null;
        }
        return (String) session.getAttribute(CommonConfig.USER_SESSION_ID);
    }

    public static HttpSession getSession(HttpServletRequest request) {

        return request.getSession();
    }

}
