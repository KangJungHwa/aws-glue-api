package com.lgdisplay.bigdata.api.service.glue.util;

import com.google.common.base.Joiner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * HTTP Request Utility.
 */
public class HttpRequestDebugUtils {

    public static String headerToString(HttpServletRequest request) {
        List headers = new ArrayList();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            headers.add(String.format("%s = %s", headerName, headerValue));
        }
        return Joiner.on("\n").join(headers);
    }

}
