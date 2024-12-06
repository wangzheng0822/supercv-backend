package com.xzgedu.supercv.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Interceptor工具类
 * @author wangzheng
 */
@Slf4j
public class InterceptorUtils {

    public static void writeResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData responseData = ResponseData.create(errorCode).data();
        log.info("[DATA] " + responseData.toString());
        String data = objectMapper.writeValueAsString(responseData);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(data);
    }

    public static Long parseLong(String str) {
        if (str == null) return null;
        return Long.parseLong(str);
    }
}
