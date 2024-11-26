package com.xzgedu.supercv.common.exception;

public class SmsCodeExpiredException extends BusinessException {

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
