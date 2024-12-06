package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录认证校验拦截器
 * @author wangzheng
 */
@Slf4j
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        boolean success = false;
        String token = request.getHeader("Authorization");
        Long uid = InterceptorUtils.parseLong(request.getHeader("uid"));
        if (!StringUtils.isBlank(token) && uid != null) {
            if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
                token = token.substring(7);
            }
            success = authService.auth(uid, token); //验证，伪造的uid和token将不能通过
        }

        if (success == false) {
            log.warn("User not login: [token={}, uid={}]", token, uid);
            InterceptorUtils.writeResponse(response, ErrorCode.AUTH_FAILED);
            return false;
        }
        return true;
    }
}