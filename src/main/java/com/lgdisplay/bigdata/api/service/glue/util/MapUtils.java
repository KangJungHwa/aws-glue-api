package com.lgdisplay.bigdata.api.service.glue.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Utility.
 */
public class MapUtils {

    /**
     * 지정한 Key Value를 가진 Map을 생성한다.
     *
     * @param key   Map 생성시 초기 key
     * @param value Map 생성시 초기 Key에 대한 Value
     * @return 새로 생성한 Map
     */
    public static Map<String, String> map(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 지정한 Key Value를 가진 Map을 생성한다.
     *
     * @param key      Map 생성시 초기 key
     * @param paramMap Map 생성시 초기 Key에 대한 Map
     * @return 새로 생성한 Map
     */
    public static Map map(String key, Map paramMap) {
        Map map = new HashMap<>();
        map.put(key, paramMap);
        return map;
    }

    /**
     * 지정한 Key Value를 가진 Map을 생성한다.
     *
     * @param key   Map 생성시 초기 key
     * @param value Map 생성시 초기 Key에 대한 Value
     * @return 새로 생성한 Map
     */
    public static Map map(String key, Boolean value) {
        Map map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    /**
     * 지정한 map을 가지고 JsonString을 만든다.
     *
     * @param map   arguments가 들어있는 Map
     * @return map을 json변환한 string
     */
    public static String mapToJson(Map<String,String> map) {
        int i=0;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, String> element : map.entrySet()) {
            i++;
            if (map.size() == i) {
                sb.append(elementToJson(element.getKey(), element.getValue(),true));
            } else {
                sb.append(elementToJson(element.getKey(), element.getValue(),false));
            }
        }
        sb.append("}");
        return sb.toString();
    }
    /**
     * 지정한 map을 가지고 JsonString을 만든다.
     *
     * @param key   map의 각요소의 Key
     * @param value   map의 각요소의 value
     * @param isLast  마지막 요소인지 체크값
     * @return map을 json변환한 string
     */
    private static String elementToJson(String key,String value, boolean isLast) {
        StringBuilder elementSb = new StringBuilder();
        elementSb.append("\"");
        elementSb.append(key);
        elementSb.append("\":");
        elementSb.append("\"");
        elementSb.append(value);
        if (isLast) {
            elementSb.append("\"");
        }else{
            elementSb.append("\",");
        }
        return elementSb.toString();
    }
}
