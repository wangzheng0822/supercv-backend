package com.xzgedu.supercv.article.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

@Data
public class Article {
    private Long id; // 文章id
    private Long uid; // 发布者id
    private Integer cateType; // 文章类型
    private String title; // 标题
    private String subTitle; // 副标题
    private String snippet; // 摘要
    private String coverImg; // 封面图片
    private Long contentId; // 正文id
    @ViewData
    private String content; //正文内容
    private boolean free; //是否免费
}