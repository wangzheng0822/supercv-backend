package com.xzgedu.supercv.admin.controller.article;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.article.utils.ArticleValidation;
import com.xzgedu.supercv.common.anotation.ViewData;
import com.xzgedu.supercv.common.exception.GenericBizException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章管理")
@RequestMapping("/admin/article")
@RestController
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "添加文章")
    @PostMapping("/add")
    public Article addArticle(@RequestHeader("uid") long uid,
                           @RequestParam("cate_type") Integer cateType,
                           @RequestParam("title") String title,
                           @RequestParam(value = "sub_title", required = false) String subTitle,
                           @RequestParam(value = "cover_img", required = false) String coverImg,
                           @RequestParam("content") String content) throws GenericBizException {
        ArticleValidation.checkArticleTitle(title);
        ArticleValidation.checkArticleContent(content);

        Article article = new Article();
        article.setUid(uid);
        article.setCateType(cateType);
        article.setTitle(title);
        article.setSubTitle(subTitle);
        article.setCoverImg(coverImg);
        article.setContent(content);
        articleService.addArticle(article);
        return article;
    }

    @Operation(summary = "更新文章")
    @PostMapping("/update")
    public void updateArticle(@RequestHeader("uid") long uid,
                              @RequestParam("article_id") long articleId,
                              @RequestParam("cate_type") Integer cateType,
                              @RequestParam("title") String title,
                              @RequestParam(value = "sub_title", required = false) String subTitle,
                              @RequestParam("content") String content) throws GenericBizException {
        ArticleValidation.checkArticleTitle(title);
        ArticleValidation.checkArticleContent(content);

        Article article = articleService.getArticleWithoutContentById(articleId);
        article.setUid(uid);
        article.setCateType(cateType);
        article.setTitle(title);
        article.setSubTitle(subTitle);
        article.setContent(content);
        articleService.updateArticle(article);
    }

    @Operation(summary = "更新文章封面图")
    @PostMapping("/update-cover-img")
    public void updateArticleCoverImg(@RequestParam("article_id") long articleId,
                                      @RequestParam("cover_img") String coverImg) throws GenericBizException {
        articleService.updateCoverImg(articleId, coverImg);
    }

    @Operation(summary = "删除文章")
    @PostMapping("/delete")
    public void deleteArticle(@RequestParam("article_id") long articleId) throws GenericBizException {
        articleService.deleteArticle(articleId);
    }
}
