package com.xzgedu.supercv.common.exception;

public class PaymentChannelDisabledException extends BizException {

    public PaymentChannelDisabledException() {
        super(ErrorCode.PAYMENT_CHANNEL_DISABLED);
    }

    public PaymentChannelDisabledException(Exception e) {
        super(e, ErrorCode.PAYMENT_CHANNEL_DISABLED);
    }

    public PaymentChannelDisabledException(String msg) {
        super(msg, ErrorCode.PAYMENT_CHANNEL_DISABLED);
    }

    public PaymentChannelDisabledException(String msg, Exception e) {
        super(msg, e, ErrorCode.PAYMENT_CHANNEL_DISABLED);
    }
}