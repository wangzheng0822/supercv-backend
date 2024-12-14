package com.xzgedu.supercv.article.service;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.domain.ArticleContent;
import com.xzgedu.supercv.article.repo.ArticleContentRepo;
import com.xzgedu.supercv.article.repo.ArticleRepo;
import com.xzgedu.supercv.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleContentRepo articleContentRepo;

    public List<Article> listArticles(String cateType, int limitOffset, int limitSize) {
        return articleRepo.listArticles(cateType, limitOffset, limitSize);
    }

    public Article getArticleById(long id) {
        Article article = articleRepo.getArticleById(id);
        ArticleContent articleContent = articleContentRepo.getArticleContentById(article.getContentId());
        article.setContent(articleContent.getContent());
        return article;
    }

    @Transactional
    public boolean  addArticle(Article article) {
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent(article.getContent());
        boolean b = articleContentRepo.addArticleContent(articleContent);
        if (!b) {
            return false;
        }

        article.setContentId(articleContent.getId());
        return articleRepo.addArticle(article);
    }

    @Transactional
    public boolean updateArticle(Article article) {
        boolean result = articleRepo.updateArticle(article);
        if (result) {
            ArticleContent articleContent = articleContentRepo.getArticleContentById(article.getContentId());
            articleContent.setContent(article.getContent());
            return articleContentRepo.updateArticleContent(articleContent);
        }
        return false;
    }

    @Transactional
    public boolean deleteArticle(long id) {
        Article article = articleRepo.getArticleById(id);
        if (Objects.nonNull(article)) {
            articleContentRepo.deleteArticleContent(article.getContentId());
        }
        return articleRepo.deleteArticle(id);
    }

}