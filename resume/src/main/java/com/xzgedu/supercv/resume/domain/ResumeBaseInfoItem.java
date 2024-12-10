package com.xzgedu.supercv.resume.domain;

import lombok.Data;

@Data
public class ResumeBaseInfoItem {
    private Long id; // 条目ID
    private Long baseInfoId; // 基本信息ID
    private String key; // 条目key
    private String value; // 条目value
    private Integer sortValue; // 排序值
}