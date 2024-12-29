package com.xzgedu.supercv.article.controller;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.common.exception.GenericBizException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/article/")
@Tag(name = "文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "获取文章分页列表")
    @GetMapping("/list")
    public Map<String, Object> listArticles(@RequestParam("cate_type") int cateType,
                                            @RequestParam("page_no") int pageNo,
                                            @RequestParam("page_size") int pageSize) {
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        int count = articleService.countArticlesByCateType(cateType);
        List<Article> articles = articleService.getArticlesByCateType(cateType, limitOffset, limitSize);
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("articles", articles);
        return resp;
    }

    @Operation(summary = "获取文章详情")
    @GetMapping("/detail")
    public Article getArticleDetailById(@RequestParam("article_id") long articleId) {
        return articleService.getArticleDetailById(articleId);
    }

    @Operation(summary = "检查是否免费")
    @GetMapping("/check-if-free")
    public boolean checkIfFreeArticle(@RequestParam("article_id") long articleId) {
        Article article = articleService.getArticleWithoutContentById(articleId);
        return article.isFree();
    }

}