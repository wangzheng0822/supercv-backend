package com.xzgedu.supercv.resume.domain;

import lombok.Data;

@Data
public class ResumeModuleItem {
    private Long id; // 子模块ID
    private Long resumeId; // 简历ID
    private Long moduleId; // 模块ID
    private String titleMajor; // 主标题
    private String titleMinor; // 次标题
    private String titleDate; // 时间
    private Integer titleSortType; // title各个部分如何排序
    private Boolean titleMajorEnabled; // 是否显示主标题
    private Boolean titleMinorEnabled; // 是否显示次标题
    private Boolean titleDateEnabled; // 是否显示日期
    private String content; // 内容
    private Integer sortValue; // 排序值
}