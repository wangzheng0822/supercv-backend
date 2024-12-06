package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.admin.service.AdminService;
import com.xzgedu.supercv.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j @Component
public class AdminInterceptor  implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        boolean isAdmin = false;
        Long uid = InterceptorUtils.parseLong(request.getHeader("uid"));
        if (uid != null) {
            isAdmin = adminService.checkIfAdmin(uid);
        }

        if (isAdmin == false) {
            log.warn("User is not admin: [uid={}]", uid);
            InterceptorUtils.writeResponse(response, ErrorCode.NO_PERMISSION_FOR_ADMIN);
            return false;
        }

        return true;
    }
}