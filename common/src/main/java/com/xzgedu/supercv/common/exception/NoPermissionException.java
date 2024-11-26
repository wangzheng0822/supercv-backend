package com.xzgedu.supercv.common.exception;

public class NoPermissionException extends BusinessException {
    public NoPermissionException() {
        super(ErrorCode.NO_PERMISSION);
    }

    public NoPermissionException(Exception e) {
        super(e, ErrorCode.NO_PERMISSION);
    }

    public NoPermissionException(String msg) {
        super(msg, ErrorCode.NO_PERMISSION);
    }

    public NoPermissionException(String msg, Exception e) {
        super(msg, e, ErrorCode.NO_PERMISSION);
    }
}
