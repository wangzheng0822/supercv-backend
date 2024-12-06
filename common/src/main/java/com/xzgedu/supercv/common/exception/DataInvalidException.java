package com.xzgedu.supercv.common.exception;

/**
 * 数据非法异常，此异常为运行时异常，因此没有继承BizException
 * @author wangzheng
 */
public class DataInvalidException extends RuntimeException {
    public ErrorCode ERROR_CODE = ErrorCode.GENERIC_DATA_INVALID;

    public DataInvalidException(ErrorCode errorCode) {
        super();
        ERROR_CODE = errorCode;
    }

    public DataInvalidException(ErrorCode errorCode, Exception e) {
        super(e);
        ERROR_CODE = errorCode;
    }

    public DataInvalidException(ErrorCode errorCode, String msg) {
        super(msg);
        ERROR_CODE = errorCode;
    }

    public DataInvalidException(ErrorCode errorCode, String msg, Exception e) {
        super(msg, e);
        ERROR_CODE = errorCode;
    }
}