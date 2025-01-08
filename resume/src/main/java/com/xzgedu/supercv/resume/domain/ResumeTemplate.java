package com.xzgedu.supercv.resume.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

@Data
public class ResumeTemplate {
    private Long id; //模板ID
    private String name; //模板名称

    private String pageFrame; //vue页面结构
    private String pageStyle; //css格式

    private Long demoResumeId; //示例简历ID
    @ViewData
    private String demoResumeThumbnailUrl;
}