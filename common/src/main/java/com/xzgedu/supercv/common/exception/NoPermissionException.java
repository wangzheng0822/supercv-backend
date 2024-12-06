package com.xzgedu.supercv.common.exception;

/**
 * 没有权限异常
 * @author wangzheng
 */
public class NoPermissionException extends BizException {
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
