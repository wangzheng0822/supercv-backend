package com.xzgedu.supercv.common.exception;

/**
 * 简历模板不存在异常
 * @author wangzheng
 */
public class ResumeTemplateNotFoundException extends BizException {

    public ResumeTemplateNotFoundException() {
        super(ErrorCode.RESUME_TEMPLATE_NOT_EXISTED);
    }

    public ResumeTemplateNotFoundException(Exception e) {
        super(e, ErrorCode.RESUME_TEMPLATE_NOT_EXISTED);
    }

    public ResumeTemplateNotFoundException(String msg) {
        super(msg, ErrorCode.RESUME_TEMPLATE_NOT_EXISTED);
    }

    public ResumeTemplateNotFoundException(String msg, Exception e) {
        super(msg, e, ErrorCode.RESUME_TEMPLATE_NOT_EXISTED);
    }
}
