package com.xzgedu.supercv.common.exception;

public class BindTelDuplicatedException extends BusinessException {

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
