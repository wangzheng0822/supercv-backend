package com.xzgedu.supercv.common.exception;

public class ProductNotFoundException extends BizException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_EXISTED);
    }

    public ProductNotFoundException(Exception e) {
        super(e, ErrorCode.PRODUCT_NOT_EXISTED);
    }

    public ProductNotFoundException(String msg) {
        super(msg, ErrorCode.PRODUCT_NOT_EXISTED);
    }

    public ProductNotFoundException(String msg, Exception e) {
        super(msg, e, ErrorCode.PRODUCT_NOT_EXISTED);
    }
}
