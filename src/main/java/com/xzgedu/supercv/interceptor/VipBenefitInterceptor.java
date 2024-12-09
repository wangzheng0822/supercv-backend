package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.vip.service.VipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class VipBenefitInterceptor implements HandlerInterceptor {

    @Autowired
    private VipService vipService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Boolean isAdmin = (Boolean) request.getAttribute("admin");
        if (isAdmin != null && isAdmin == true) return true;

        Long uid = InterceptorUtils.parseLong(request.getHeader("uid"));
        boolean permit = uid != null && vipService.permitValidVip(uid);
        if (!permit) {
            log.warn("No Permission for non-vip user: [uid={}]", uid);
            InterceptorUtils.writeResponse(response, ErrorCode.NO_PERMISSION);
            return false;
        }
        return true;
    }
}