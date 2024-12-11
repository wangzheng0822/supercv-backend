package com.xzgedu.supercv.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author qingy
 * @date 2024-12-10
 */
@Getter
@AllArgsConstructor
public enum ArticleTypeEnum {
    EXPERT_SERVICE("EXPERT_SERVICE", "专家服务"),
    CASE_REFERENCE("CASE_REFERENCE", "案例参考");

    private final String value;
    private final String desc;

    public static boolean isValid(String value) {
        for (ArticleTypeEnum articleTypeEnum : ArticleTypeEnum.values()) {
            if (articleTypeEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
