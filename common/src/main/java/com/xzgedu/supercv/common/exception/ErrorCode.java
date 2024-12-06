package com.xzgedu.supercv.common.exception;

/**
 * 5位错误码，格式为：[1位表示错误类型]+[2位表示业务]+[2位表示具体异常]
 * @author wangzheng
 */
public enum ErrorCode {
    /**
     * ****** 2xxxx操作成功 ***************
     */
    SUCCESS(20000, "操作成功"),

    /**
     * ****** 3xxxx可预期业务异常 ************
     */
    // 权限
    AUTH_FAILED(30101, "请先登录"),
    PHONE_VERIFY_FAILED(30102, "没有绑定手机号码"),
    NO_PERMISSION(30103, "没有权限"),
    TOO_FREQUENT_REQUEST(30104, "请求过于频繁"),
    DENY_FOR_PROD(30105, "生产环境禁止使用此功能"),
    NO_PERMISSION_FOR_ADMIN(30106, "非管理员禁止访问"),

    // 用户信息
    NICKNAME_INVALID(30201, "昵称不符合要求(长度限于1个字~10个字)"),
    TELEPHONE_INVALID(30202, "手机号码格式不对"),
    BIND_TEL_DUPLICATED(30203, "手机号码已绑定其他账号"),

    // 短信验证
    SMS_CODE_INVALID(30301, "短信验证码格式不对"),
    SMS_CODE_UNMATCHED(30302, "验证码错误"),
    SMS_CODE_EXPIRED(30304, "验证码过期"),

    // 输入数据验证
    GENERIC_DATA_INVALID(30401, "数据错误"),
    DATA_SHOULD_NOT_EMPTY(30402, "数据不应为空"),

    /**
     * ******** 4xxxx不应该出现的业务异常 ***********
     */
    GENERIC_BIZ_FAILED(40000, "操作失败"),

    // 用户信息
    USER_NOT_EXISTED(40101, "用户不存在"),

    // 微信相关
    FETCH_WX_USER_INFO_FAILED(40201, "微信用户信息获取失败"),

    // 短信验证
    SMS_CODE_SEND_FAILED(40301, "验证码发送失败"),

    /**
     * ******** 5xxxx系统级别异常 ***************
     */
    INTERNAL_SERVER_ERROR(50000, "系统错误"),
    PARAMETER_INVALID(50001, "缺少参数或参数类型不正确"),

    /**
     * ******** 99999未知异常 ***************
     */
    ERROR_UNKNOWN(99999, "未知错误"),

    /**
     * 为了兼容HTTP Code，做到一目了然，这几个返回值比较特殊
     */
    ERROR_400(400, "接口参数不正确"),
    ERROR_404(404, "接口不存在，请仔细检查url"),
    ERROR_405(405, "未正确指定HTTP Method (GET or POST)");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static ErrorCode valueOf(int code) {
        for (ErrorCode type : ErrorCode.values()) {
            if (code == type.code) return type;
        }
        return ERROR_UNKNOWN;
    }
}