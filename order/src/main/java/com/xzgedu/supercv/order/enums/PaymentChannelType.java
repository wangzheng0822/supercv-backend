package com.xzgedu.supercv.order.enums;

public enum PaymentChannelType {
    UNKNOWN(0, "未知"),
    WXPAY(1, "微信支付"),
    ALIPAY(2, "支付宝"),
    MOCK_PAYMENT(3, "Mock支付");

    private final int value;
    private final String name;

    PaymentChannelType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static PaymentChannelType of(int value) {
        PaymentChannelType[] types = PaymentChannelType.values();
        for (PaymentChannelType type : types) {
            if (value == type.getValue()) return type;
        }
        return UNKNOWN;
    }
}