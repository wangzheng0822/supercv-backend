package com.xzgedu.supercv.common.anotation;

import java.lang.annotation.*;

/**
 * 标记Domain中的非数据库字段
 * @author wangzheng
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface ViewData {
}