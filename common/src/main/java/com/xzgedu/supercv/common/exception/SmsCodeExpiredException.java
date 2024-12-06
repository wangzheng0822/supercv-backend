package com.xzgedu.supercv.common.exception;

/**
 * 短信验证码过期异常
 * @author wangzheng
 */
public class SmsCodeExpiredException extends BizException {

    public SmsCodeExpiredException() {
        super(ErrorCode.SMS_CODE_EXPIRED);
    }

    public SmsCodeExpiredException(Exception e) {
        super(e, ErrorCode.SMS_CODE_EXPIRED);
    }

    public SmsCodeExpiredException(String msg) {
        super(msg, ErrorCode.SMS_CODE_EXPIRED);
    }

    public SmsCodeExpiredException(String msg, Exception e) {
        super(msg, e, ErrorCode.SMS_CODE_EXPIRED);
    }
}
