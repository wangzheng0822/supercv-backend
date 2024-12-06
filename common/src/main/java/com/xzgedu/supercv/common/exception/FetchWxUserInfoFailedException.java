package com.xzgedu.supercv.common.exception;

/**
 * 微信登陆失败异常
 * @author wangzheng
 */
public class FetchWxUserInfoFailedException extends BizException {
    public FetchWxUserInfoFailedException() {
        super(ErrorCode.FETCH_WX_USER_INFO_FAILED);
    }

    public FetchWxUserInfoFailedException(Exception e) {
        super(e, ErrorCode.FETCH_WX_USER_INFO_FAILED);
    }

    public FetchWxUserInfoFailedException(String msg) {
        super(msg, ErrorCode.FETCH_WX_USER_INFO_FAILED);
    }

    public FetchWxUserInfoFailedException(String msg, Exception e) {
        super(msg, e, ErrorCode.FETCH_WX_USER_INFO_FAILED);
    }
}
