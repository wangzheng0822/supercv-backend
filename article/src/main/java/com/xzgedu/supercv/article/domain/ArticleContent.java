package com.xzgedu.supercv.article.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleContent implements Serializable {
    private Long id; // 正文id
    private String content; // 正文内容
}