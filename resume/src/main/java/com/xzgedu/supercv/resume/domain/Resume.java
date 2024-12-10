package com.xzgedu.supercv.resume.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

import java.util.List;

@Data
public class Resume {
    private Long id; // 简历ID
    private Long uid; // 简历归属用户ID
    private String name; // 简历名称

    private Long templateId; // 模板ID
    @ViewData
    private String templateName; //模板名称
    @ViewData
    private String templateCssName; //模板css文件名

    private String originalResumeUrl; // 简历文件url(pdf/word等)
    private String thumbnailUrl; // 简历缩略图url

    private Integer pageMarginHorizontal; // 左右页边距
    private Integer pageMarginVertical; // 上下页边距
    private Integer moduleMargin; // 模块间距
    private String themeColor; // 主题色
    private Integer fontSize; // 字体大小
    private String fontFamily; // 字体
    private Integer lineHeight; // 行高

    private Boolean templateDemo; // 是否是模板示例简历

    @ViewData
    private ResumeBaseInfo baseInfo; //基本信息
    @ViewData
    private List<ResumeModule> modules; //各个模块
}
