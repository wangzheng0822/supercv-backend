package com.xzgedu.supercv.common.exception;

public class FetchWxUserInfoFailedException extends BusinessException {
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
