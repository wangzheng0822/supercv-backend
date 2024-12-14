package com.xzgedu.supercv.common.exception;

public class PaymentException extends BizException {

    public PaymentException() {
        super(ErrorCode.PAYMENT_FAILED);
    }

    public PaymentException(Exception e) {
        super(e, ErrorCode.PAYMENT_FAILED);
    }

    public PaymentException(String msg) {
        super(msg, ErrorCode.PAYMENT_FAILED);
    }

    public PaymentException(String msg, Exception e) {
        super(msg, e, ErrorCode.PAYMENT_FAILED);
    }
}
