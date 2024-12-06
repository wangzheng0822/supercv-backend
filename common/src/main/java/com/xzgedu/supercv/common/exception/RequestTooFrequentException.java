package com.xzgedu.supercv.common.exception;

/**
 * 请求过于频繁异常
 * @author wangzheng
 */
public class RequestTooFrequentException extends BizException {
    public RequestTooFrequentException() {
        super(ErrorCode.TOO_FREQUENT_REQUEST);
    }

    public RequestTooFrequentException(Exception e) {
        super(e, ErrorCode.TOO_FREQUENT_REQUEST);
    }

    public RequestTooFrequentException(String msg) {
        super(msg, ErrorCode.TOO_FREQUENT_REQUEST);
    }

    public RequestTooFrequentException(String msg, Exception e) {
        super(msg, e, ErrorCode.TOO_FREQUENT_REQUEST);
    }
}
