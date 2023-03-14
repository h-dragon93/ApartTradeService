package com.estate.hdragon.apart.api;

import com.estate.hdragon.apart.util.HttpUrlConnectionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLEncoder;

@Service
public class ApartApiService {

    @Value("${apart.serviceKey}")
    String serviceKey;

    @Value("${apart.serviceUrl}")
    String serviceUrl;

    public StringBuilder callApartInfo(String lawd_cd, String deal_ymd) throws Throwable{

        StringBuilder urlBuilder = new StringBuilder(serviceUrl); /*URL*/

        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=").append(serviceKey); /*Service Key*/
        urlBuilder.append("&").append(URLEncoder.encode("LAWD_CD", "UTF-8")).append("=").append(URLEncoder.encode(lawd_cd, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(deal_ymd, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        StringBuilder sb = new StringBuilder();

        StringBuilder tradeData = HttpUrlConnectionUtil.getInputStreamData(url, sb);

        return tradeData;

    }

}
