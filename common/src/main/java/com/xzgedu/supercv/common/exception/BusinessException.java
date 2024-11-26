package com.xzgedu.supercv.common.exception;


public abstract class BusinessException extends Exception {
    public ErrorCode ERROR_CODE;

    public BusinessException(ErrorCode errorCode) {
        super();
        this.ERROR_CODE = errorCode;
    }

    public BusinessException(Exception e, ErrorCode errorCode) {
        super(e);
        this.ERROR_CODE = errorCode;
    }

    public BusinessException(String msg, ErrorCode errorCode) {
        super(msg);
        this.ERROR_CODE = errorCode;
    }

    public BusinessException(String msg, Exception e, ErrorCode errorCode) {
        super(msg, e);
        this.ERROR_CODE = errorCode;
    }
}
