package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 保证非管理员用户只能访问自己的数据
 * @author wangzheng
 */
@Slf4j
@Component
public class IdentifierInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        String selfUid = request.getHeader("uid");
        String targetUid = request.getParameter("uid");
        if (StringUtils.isBlank(targetUid)) return true;
        if (targetUid.equals(selfUid)) return true;
        boolean isAdmin = !StringUtils.isBlank(selfUid) && adminService.checkIfAdmin(InterceptorUtils.parseLong(selfUid));
        if (isAdmin) return true;

        log.warn("No permission to access data of uid={} by uid={}", targetUid, selfUid);
        InterceptorUtils.writeResponse(response, ErrorCode.NO_PERMISSION);
        return false;
    }
}
