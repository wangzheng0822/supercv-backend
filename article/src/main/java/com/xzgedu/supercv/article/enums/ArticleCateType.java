package com.xzgedu.supercv.article.enums;

public enum ArticleCateType {
    UNKNOWN(0, "未知"),
    RESUME_EXAMPLE(1, "简历案例"),
    EXPERT_GUIDE(2, "专家服务");

    private final int value;
    private final String name;

    ArticleCateType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static ArticleCateType of(int value) {
        ArticleCateType[] types = ArticleCateType.values();
        for (ArticleCateType type : types) {
            if (value == type.getValue()) return type;
        }
        return UNKNOWN;
    }
}
