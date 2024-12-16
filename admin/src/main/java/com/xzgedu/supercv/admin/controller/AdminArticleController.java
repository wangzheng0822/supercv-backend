package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.article.utils.ArticleValidation;
import com.xzgedu.supercv.common.exception.GenericBizException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name="文章管理")
@RequestMapping("/admin/article")
@RestController
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "添加文章")
    @PostMapping("/add")
    public void addArticle(@RequestBody Article article) throws GenericBizException {
        ArticleValidation.checkArticleTitle(article.getTitle());
        ArticleValidation.checkArticleContent(article.getContent());
        articleService.addArticle(article);
    }

    @Operation(summary = "更新文章")
    @PostMapping("/update")
    public void updateArticle(@RequestBody Article article) throws GenericBizException {
        ArticleValidation.checkArticleTitle(article.getTitle());
        ArticleValidation.checkArticleContent(article.getContent());
        articleService.updateArticle(article);
    }

    @Operation(summary = "删除文章")
    @PostMapping("/delete")
    public void deleteArticle(@RequestParam("article_id") long articleId) throws GenericBizException {
        articleService.deleteArticle(articleId);
    }
}
