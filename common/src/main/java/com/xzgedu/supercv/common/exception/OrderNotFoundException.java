package com.xzgedu.supercv.common.exception;

public class OrderNotFoundException extends BizException {
    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_EXISTED);
    }

    public OrderNotFoundException(Exception e) {
        super(e, ErrorCode.ORDER_NOT_EXISTED);
    }

    public OrderNotFoundException(String msg) {
        super(msg, ErrorCode.ORDER_NOT_EXISTED);
    }

    public OrderNotFoundException(String msg, Exception e) {
        super(msg, e, ErrorCode.ORDER_NOT_EXISTED);
    }

}
