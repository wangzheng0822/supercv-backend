package com.xzgedu.supercv.common.exception;

/**
 * 短信验证码发送失败异常
 * @author wangzheng
 */
public class SendSmsCodeFailedException extends BizException {

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
