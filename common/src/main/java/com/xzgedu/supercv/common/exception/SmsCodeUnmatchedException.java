package com.xzgedu.supercv.common.exception;

public class SmsCodeUnmatchedException extends BusinessException {

    public SmsCodeUnmatchedException() {
        super(ErrorCode.SMS_CODE_UNMATCHED);
    }

    public SmsCodeUnmatchedException(Exception e) {
        super(e, ErrorCode.SMS_CODE_UNMATCHED);
    }

    public SmsCodeUnmatchedException(String msg) {
        super(msg, ErrorCode.SMS_CODE_UNMATCHED);
    }

    public SmsCodeUnmatchedException(String msg, Exception e) {
        super(msg, e, ErrorCode.SMS_CODE_UNMATCHED);
    }
}
