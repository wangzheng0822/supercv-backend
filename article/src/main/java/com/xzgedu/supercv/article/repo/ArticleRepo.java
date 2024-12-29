package com.xzgedu.supercv.article.repo;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepo {

    @Autowired
    private ArticleMapper articleMapper;

    public List<Article> getArticlesByCateType(int cateType, int limitOffset, int limitSize) {
        return articleMapper.selectArticlesByCateType(cateType, limitOffset, limitSize);
    }

    public int countArticlesByCateType(int cateType) {
        return articleMapper.countArticlesByCateType(cateType);
    }

    public Article getArticleById(long id) {
        return articleMapper.selectArticleById(id);
    }

    public boolean addArticle(Article article) {
        return articleMapper.insertArticle(article) == 1;
    }

    public boolean updateArticle(Article article) {
        return articleMapper.updateArticle(article) == 1;
    }

    public boolean updateCoverImg(long articleId, String coverImg) {
        return articleMapper.updateCoverImg(articleId, coverImg) == 1;
    }

    public boolean deleteArticle(long id) {
        return articleMapper.deleteArticle(id) == 1;
    }
}