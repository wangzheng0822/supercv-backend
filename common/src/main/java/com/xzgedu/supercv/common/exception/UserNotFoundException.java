package com.xzgedu.supercv.common.exception;

/**
 * 用户不存在异常
 * @author wangzheng
 */
public class UserNotFoundException extends BizException {

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
