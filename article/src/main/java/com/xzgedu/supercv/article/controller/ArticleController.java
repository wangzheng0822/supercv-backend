package com.xzgedu.supercv.article.controller;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.common.enums.ArticleTypeEnum;
import com.xzgedu.supercv.common.exception.ArticleCateTypeErr;
import com.xzgedu.supercv.common.exception.ArticleContentErr;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/article/")
@Tag(name = "文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "获取文章分页列表")
    @GetMapping("/list")
    public List<Article> list(@RequestHeader(value = "cateType") String cateType,
                              @RequestParam("page_no") Integer pageNo,
                              @RequestParam("page_size") Integer pageSize) throws ArticleCateTypeErr {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        if (!ArticleTypeEnum.isValid(cateType)) {
            throw new ArticleCateTypeErr();
        }

        return articleService.listArticles(cateType, limitOffset, limitSize);
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/detail")
    public Article getArticleById(@RequestParam("article_id") long articleId) {
        return articleService.getArticleById(articleId);
    }

    @Operation(summary = "添加文章")
    @PostMapping("/add")
    public boolean addArticle(@RequestBody Article article) throws ArticleCateTypeErr, ArticleContentErr {
        if (!ArticleTypeEnum.isValid(article.getCateType())) {
            throw new ArticleCateTypeErr();
        }
        if (StringUtils.isEmpty(article.getTitle()) || StringUtils.isEmpty(article.getContent())) {
            throw new ArticleContentErr();
        }
        return articleService.addArticle(article);
    }

    @Operation(summary = "更新文章")
    @PostMapping("/update")
    public boolean updateArticle(@RequestBody Article article) throws ArticleCateTypeErr, ArticleContentErr {
        if (!ArticleTypeEnum.isValid(article.getCateType())) {
            throw new ArticleCateTypeErr();
        }
        if (StringUtils.isEmpty(article.getTitle()) || StringUtils.isEmpty(article.getContent())) {
            throw new ArticleContentErr();
        }
        return articleService.updateArticle(article);
    }

    @Operation(summary = "删除文章")
    @PostMapping("/delete")
    public boolean deleteArticle(@RequestParam("article_id") long articleId) {
        return articleService.deleteArticle(articleId);
    }

}