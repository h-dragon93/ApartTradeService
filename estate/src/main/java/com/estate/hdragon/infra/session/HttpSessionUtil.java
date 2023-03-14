package com.estate.hdragon.infra.session;

import com.estate.hdragon.infra.common.CommonConfig;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpSession;

public class HttpSessionUtil {

    public static boolean isLogin(HttpSession session) {
        Object sessionUser = session.getAttribute(CommonConfig.USER_SESSION_KEY);
        if (sessionUser == null) {
            return false;
        }
        return true;
    }

    public static String getSessionKeyFromSession(HttpSession session) {
        if(!isLogin(session)) {
            return null;
        }
        return (String) session.getAttribute(CommonConfig.USER_SESSION_KEY);
    }

}
