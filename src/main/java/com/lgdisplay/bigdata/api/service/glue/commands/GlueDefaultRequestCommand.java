package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 모든 Request Command의 부모 클래스.
 * 모든 Request Command에서 사용하는 클래스는 모두 여기에 정의한다.
 */
public class GlueDefaultRequestCommand {

    /**
     * S3 Resource Arn Prefix
     */
    public static final String S3_RESOURCE_PREFIX = "arn:aws:s3:";

    /**
     * DynamoDB Resource Arn Prefix
     */
    public static final String DYNAMODB_RESOURCE_PREFIX = "arn:aws:dynamodb:";

    /**
     * Athena Resource Arn Prefix
     */
    public static final String ATHENA_RESOURCE_PREFIX = "arn:aws:athena:";

    /**
     * Glue Resource Arn Prefix
     */
    public static final String GLUE_RESOURCE_PREFIX = "arn:aws:glue:";

    /**
     * Jackson ObjectMapper
     */
    private ObjectMapper mapper;

    /**
     * HTTP Request Header에서 Access Key에 해당하는 호출한 사용자의 Username을 추출한다.
     *
     * @param request HTTP Request
     * @return 호출한 사용자의 Username
     */
    public static String getUsername(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String value = org.apache.commons.lang.StringUtils.substringBetween(header, "Credential=", "aws4_request");
        return org.apache.commons.lang.StringUtils.splitPreserveAllTokens(value, "/")[0];
    }

    /**
     * 지정한 Map에서 이름에 해당하는 문자열 값을 반환한다.
     *
     * @param params       Map
     * @param paramName    파라미터명
     * @param defaultValue 기본값
     * @return 파라미터값
     */
    public static String getString(Map<String, String> params, String paramName, String defaultValue) {
        if (!StringUtils.hasLength(params.get(paramName))) {
            return defaultValue;
        }
        return params.get(paramName);
    }

    /**
     * 지정한 Map에서 이름에 해당하는 Boolean 값을 반환한다.
     *
     * @param params       Map
     * @param paramName    파라미터명
     * @param defaultValue 기본값
     * @return 파라미터값
     */
    public static boolean getBoolean(Map<String, String> params, String paramName, boolean defaultValue) {
        if (!StringUtils.hasLength(params.get(paramName))) {
            return defaultValue;
        }
        return Boolean.parseBoolean(params.get(paramName));
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
