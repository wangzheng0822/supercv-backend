package com.xzgedu.supercv.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.common.exception.BusinessException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.xzgedu.supercv")
public class ResponseWrapAdvice implements ResponseBodyAdvice<Object> {
    // Thread-safe
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        ResponseData data;
        if (body instanceof String) {
             data = ResponseData.create(ErrorCode.SUCCESS).data(body);
             logResponseData(data);
             return objectMapper.writeValueAsString(data);
        } else if (body instanceof ResponseData) {
            data = (ResponseData) body;
        } else {
            data = ResponseData.create(ErrorCode.SUCCESS).data(body);
        }

        logResponseData(data);
        return data;
    }

    private void logResponseData(ResponseData data) {
        if (data.toString().length() > 512) {
            log.info("[DATA] " + data.toString().substring(0, 512)
                    + "...<" + data.toString().length() + ">");
        } else {
            log.info("[DATA] " + data.toString());
        }
    }

    @ResponseBody
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseData handleBusinessException(BusinessException e) {
        log.warn(e.ERROR_CODE + ": " + e.toString()); // 注意：此处不要把exception的stack trace打印出来
        return ResponseData.create(e.ERROR_CODE).data();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseData handleSystemException(Exception e) {
        log.error("Internal server error", e);
        return ResponseData.create(ErrorCode.INTERNAL_SERVER_ERROR).data();
    }
}