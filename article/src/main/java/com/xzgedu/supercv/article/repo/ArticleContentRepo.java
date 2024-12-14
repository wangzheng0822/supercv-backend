package com.xzgedu.supercv.article.repo;

import com.xzgedu.supercv.article.domain.ArticleContent;
import com.xzgedu.supercv.article.mapper.ArticleContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleContentRepo {

    @Autowired
    private ArticleContentMapper articleContentMapper;

    public ArticleContent getArticleContentById(long id) {
        return articleContentMapper.selectArticleContentById(id);
    }

    public boolean addArticleContent(ArticleContent articleContent) {
        return articleContentMapper.insertArticleContent(articleContent) == 1;
    }

    public boolean updateArticleContent(ArticleContent articleContent) {
        return articleContentMapper.updateArticleContent(articleContent) == 1;
    }

    public boolean deleteArticleContent(long id) {
        return articleContentMapper.deleteArticleContent(id) == 1;
    }
}