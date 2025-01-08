package com.xzgedu.supercv.resume.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

import java.util.List;

@Data
public class Resume {
    private Long id;
    private Long uid;
    private String name;
    private String originalResumeUrl; // 简历文件url(pdf/word等)
    private String thumbnailUrl; // 简历缩略图url

    private Long templateId;
    @ViewData
    private String templateName;
    @ViewData
    private String pageFrame; //模板vue页面结构
    @ViewData
    private String pageStyle; //模板css文件名

    private Integer pageMarginHorizontal; // 左右页边距
    private Integer pageMarginVertical; // 上下页边距
    private Integer moduleMargin; // 模块间距
    private String themeColor; // 主题色
    private Integer fontSize; // 字体大小
    private String fontFamily; // 字体
    private Integer lineHeight; // 行高

    private boolean templateDemo; // 是否是模板示例简历

    @ViewData
    private ResumeBaseInfo baseInfo; //基本信息
    @ViewData
    private List<ResumeModule> modules; //各个模块
}
