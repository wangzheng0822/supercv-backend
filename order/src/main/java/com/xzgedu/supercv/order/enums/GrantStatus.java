package com.xzgedu.supercv.order.enums;

public enum GrantStatus {
    UNKNOWN(0, "未知状态"),
    PENDING(1, "待授权"),
    SUCCESS(2, "已授权"),
    FAILED(3, "授权失败");

    private final int value;
    private final String name;

    GrantStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static GrantStatus of(int value) {
        GrantStatus[] types = GrantStatus.values();
        for (GrantStatus type : types) {
            if (value == type.getValue()) return type;
        }
        return UNKNOWN;
    }
}
