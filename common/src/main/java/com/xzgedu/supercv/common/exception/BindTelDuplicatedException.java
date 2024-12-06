package com.xzgedu.supercv.common.exception;

/**
 * 重复绑定同一手机号码异常
 * @author wangzheng
 */
public class BindTelDuplicatedException extends BizException {

    public BindTelDuplicatedException() {
        super(ErrorCode.BIND_TEL_DUPLICATED);
    }

    public BindTelDuplicatedException(Exception e) {
        super(e, ErrorCode.BIND_TEL_DUPLICATED);
    }

    public BindTelDuplicatedException(String msg) {
        super(msg, ErrorCode.BIND_TEL_DUPLICATED);
    }

    public BindTelDuplicatedException(String msg, Exception e) {
        super(msg, e, ErrorCode.BIND_TEL_DUPLICATED);
    }
}
