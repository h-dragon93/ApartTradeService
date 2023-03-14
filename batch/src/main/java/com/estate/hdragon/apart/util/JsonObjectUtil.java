package com.estate.hdragon.apart.util;

import org.json.JSONObject;
import org.json.XML;

public class JsonObjectUtil {

    public static String getJsonFromStringBuilder(String stringBuilder) {
        JSONObject xmlJSONObj = XML.toJSONObject(stringBuilder.toString());
        String jsonPrettyPrintString = xmlJSONObj.toString(4);   // indent 띄어쓰기 여백 size 4

        return jsonPrettyPrintString;
    }

    public static JSONObject getTranslatedJsonObject(JSONObject arrayElement) {

        putAndRemove(arrayElement, "lawdCd", "지역코드");
        putAndRemove(arrayElement, "year", "년");
        putAndRemove(arrayElement, "cancelDealDay", "해제사유발생일");
        putAndRemove(arrayElement, "dongName", "법정동");
        putAndRemove(arrayElement, "dealAmount", "거래금액");
        putAndRemove(arrayElement, "apartName", "아파트");
        putAndRemove(arrayElement, "dealerAddress", "중개사소재지");
        putAndRemove(arrayElement, "reqGbn", "거래유형");
        putAndRemove(arrayElement, "jibun", "지번");
        putAndRemove(arrayElement, "cancelYn", "해제여부");
        putAndRemove(arrayElement, "month", "월");
        putAndRemove(arrayElement, "day", "일");
        putAndRemove(arrayElement, "floor", "층");
        putAndRemove(arrayElement, "buildYear", "건축년도");
        putAndRemove(arrayElement, "exclusiveAreaUse", "전용면적");

        return arrayElement;

    }

    public static JSONObject getRedisJsonObject(JSONObject arrayElement) {
        JSONObject redisJsonObject = new JSONObject();
        redisJsonObject.put("tradeId", arrayElement.get("지역코드").toString() + arrayElement.get("지번") + arrayElement.get("월")+ arrayElement.get("일")+ arrayElement.get("층"));
        putTarget(arrayElement, redisJsonObject, "dealAmount", "거래금액");
        putTarget(arrayElement, redisJsonObject, "cancelYn", "해제여부");
        putTarget(arrayElement, redisJsonObject, "cancelDealDay", "해제사유발생일");
        return redisJsonObject;
    }

    private static JSONObject putAndRemove(JSONObject arrayElement, String translated, String korean) {

        arrayElement.put(translated, arrayElement.get(korean));
        arrayElement.remove(korean);

        return arrayElement;
    }

    private static JSONObject putTarget(JSONObject originElement, JSONObject targetElement, String translated, String korean) {

        targetElement.put(translated, originElement.get(korean));

        return targetElement;
    }
}
