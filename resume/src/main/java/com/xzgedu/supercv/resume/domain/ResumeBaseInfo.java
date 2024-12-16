package com.xzgedu.supercv.resume.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResumeBaseInfo {
    private Long id; // 基本信息ID
    private Long resumeId; // 简历ID
    private String headImgUrl; // 头像
    private Boolean headImgEnabled; // 是否显示头像
    private Integer headImgLayout; // 头像布局，默认居右，取值居左、居右、居上
    private Integer itemLayout; // 条目布局，默认居中，取值左对齐，右对齐，居中对齐
    private Boolean enabled; // 是否启用

    private List<ResumeBaseInfoItem> items = new ArrayList<>(); //各个条目
}
