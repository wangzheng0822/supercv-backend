package com.xzgedu.supercv.order.enums;

public enum PaymentStatus {
    UNKNOWN(0, "未知"),
    PENDING(1, "待支付"),
    PAID(2, "已支付"),
    FAILED(3, "支付失败"),
    CLOSED(4, "过期"),
    REFUND(5, "退款");

    private final int value;
    private final String name;

    PaymentStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static PaymentStatus of(int value) {
        PaymentStatus[] types = PaymentStatus.values();
        for (PaymentStatus type : types) {
            if (value == type.getValue()) return type;
        }
        return UNKNOWN;
    }
}
