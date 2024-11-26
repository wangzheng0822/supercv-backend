package com.xzgedu.supercv.common.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_EXISTED);
    }

    public UserNotFoundException(Exception e) {
        super(e, ErrorCode.USER_NOT_EXISTED);
    }

    public UserNotFoundException(String msg) {
        super(msg, ErrorCode.USER_NOT_EXISTED);
    }

    public UserNotFoundException(String msg, Exception e) {
        super(msg, e, ErrorCode.USER_NOT_EXISTED);
    }
}
