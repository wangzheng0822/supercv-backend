package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.admin.service.AdminService;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ResumeOwnershipInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ResumeService resumeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        Long selfUid = InterceptorUtils.parseLong(request.getHeader("uid"));

        //管理员豁免
        boolean isAdmin = selfUid != null && adminService.checkIfAdmin(selfUid);
        if (isAdmin) return true;

        //检查归属权
        Long resumeId = InterceptorUtils.parseLong(request.getParameter("resume_id"));
        if (resumeId != null) {
            Resume resume = resumeService.selectResumeById(resumeId);
            if (resume != null && resume.getUid().equals(selfUid)) {
                return true;
            }
        }

        //非owner拒绝执行
        log.warn("No permission to access resume {} by uid={}", resumeId, selfUid);
        InterceptorUtils.writeResponse(response, ErrorCode.NO_PERMISSION);
        return false;
    }
}