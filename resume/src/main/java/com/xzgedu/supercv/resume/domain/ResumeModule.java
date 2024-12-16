package com.xzgedu.supercv.resume.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResumeModule {
    private Long id; // 模块ID
    private Long resumeId; // 简历ID
    private String title; // 模块标题
    private Integer sortValue; // 排序值
    private Boolean defaultModule; // 是否是默认模块，默认模块不可删除
    private Boolean enabled; // 是否启用

    private List<ResumeModuleItem> moduleItems = new ArrayList<>();
}
