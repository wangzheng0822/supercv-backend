package com.xzgedu.supercv.common.exception;

/**
 * 通用的业务异常
 * @author wangzheng
 */
public class GenericBizException extends BizException {
    public GenericBizException() {
        super(ErrorCode.GENERIC_BIZ_FAILED);
    }

    public GenericBizException(Exception e) {
        super(e, ErrorCode.GENERIC_BIZ_FAILED);
    }

    public GenericBizException(String msg) {
        super(msg, ErrorCode.GENERIC_BIZ_FAILED);
    }

    public GenericBizException(String msg, Exception e) {
        super(msg, e, ErrorCode.GENERIC_BIZ_FAILED);
    }
}