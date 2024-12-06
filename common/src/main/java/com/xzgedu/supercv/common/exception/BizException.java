package com.xzgedu.supercv.common.exception;

/**
 * 业务异常父类
 * @author wangzheng
 */
public class BizException extends Exception {
    public ErrorCode ERROR_CODE;

    public BizException(ErrorCode errorCode) {
        super();
        this.ERROR_CODE = errorCode;
    }

    public BizException(Exception e, ErrorCode errorCode) {
        super(e);
        this.ERROR_CODE = errorCode;
    }

    public BizException(String msg, ErrorCode errorCode) {
        super(msg);
        this.ERROR_CODE = errorCode;
    }

    public BizException(String msg, Exception e, ErrorCode errorCode) {
        super(msg, e);
        this.ERROR_CODE = errorCode;
    }
}
