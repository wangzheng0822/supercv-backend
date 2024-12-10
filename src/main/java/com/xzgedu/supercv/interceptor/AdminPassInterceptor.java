package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AdminPassInterceptor implements HandlerInterceptor {

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
        request.setAttribute("admin", isAdmin);

        return true;
    }
}