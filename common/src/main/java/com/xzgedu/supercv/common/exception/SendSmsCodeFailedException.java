package com.xzgedu.supercv.common.exception;

public class SendSmsCodeFailedException extends BusinessException {

    public SendSmsCodeFailedException() {
        super(ErrorCode.SMS_CODE_SEND_FAILED);
    }

    public SendSmsCodeFailedException(Exception e) {
        super(e, ErrorCode.SMS_CODE_SEND_FAILED);
    }

    public SendSmsCodeFailedException(String msg) {
        super(msg, ErrorCode.SMS_CODE_SEND_FAILED);
    }

    public SendSmsCodeFailedException(String msg, Exception e) {
        super(msg, e, ErrorCode.SMS_CODE_SEND_FAILED);
    }
}
