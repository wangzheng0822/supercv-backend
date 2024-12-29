package com.xzgedu.supercv.article.service;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.domain.ArticleContent;
import com.xzgedu.supercv.article.repo.ArticleContentRepo;
import com.xzgedu.supercv.article.repo.ArticleRepo;
import com.xzgedu.supercv.article.utils.MarkdownConverter;
import com.xzgedu.supercv.common.exception.GenericBizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private static final int MAX_SNIPPET_LENGTH = 196;

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleContentRepo articleContentRepo;

    @Autowired
    private MarkdownConverter markdownConverter;

    public List<Article> getArticlesByCateType(int cateType, int limitOffset, int limitSize) {
        return articleRepo.getArticlesByCateType(cateType, limitOffset, limitSize);
    }

    public int countArticlesByCateType(int cateType) {
        return articleRepo.countArticlesByCateType(cateType);
    }

    public Article getArticleDetailById(long id) {
        Article article = articleRepo.getArticleById(id);
        ArticleContent articleContent = articleContentRepo.getArticleContentById(article.getContentId());
        article.setContent(articleContent.getContent());
        return article;
    }

    public Article getArticleWithoutContentById(long id) {
        return articleRepo.getArticleById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addArticle(Article article) throws GenericBizException {
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent(article.getContent());
        if (!articleContentRepo.addArticleContent(articleContent)) {
            throw new GenericBizException("Failed to add article content: " + article.getTitle());
        }

        String snippet = markdownConverter.toPlainText(article.getContent());
        if (snippet.length() > MAX_SNIPPET_LENGTH) {
            snippet = snippet.substring(0, MAX_SNIPPET_LENGTH) + "...<a>查看全文</a>";
        }
        article.setSnippet(snippet);
        article.setContentId(articleContent.getId());
        if (!articleRepo.addArticle(article)) {
            throw new GenericBizException("Failed to add article: " + article.getTitle());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Article article) throws GenericBizException {
        String snippet = markdownConverter.toPlainText(article.getContent());
        if (snippet.length() > MAX_SNIPPET_LENGTH) {
            snippet = snippet.substring(0, MAX_SNIPPET_LENGTH) + "...<a>查看全文</a>";
        }
        article.setSnippet(snippet);
        if (!articleRepo.updateArticle(article)) {
            throw new GenericBizException("Failed to update article: " + article.getId());
        }

        ArticleContent articleContent = new ArticleContent();
        articleContent.setId(article.getContentId());
        articleContent.setContent(article.getContent());
        if (!articleContentRepo.updateArticleContent(articleContent)) {
            throw new GenericBizException("Failed to update article content: " + article.getContentId());
        }
    }

    public void updateCoverImg(long articleId, String coverImg) throws GenericBizException {
        if (!articleRepo.updateCoverImg(articleId, coverImg)) {
            throw new GenericBizException("Failed to update article cover img: " + articleId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(long articleId) throws GenericBizException {
        Article article = articleRepo.getArticleById(articleId);
        if (article == null) {
            return;
        }
        if (!articleContentRepo.deleteArticleContent(article.getContentId())) {
            throw new GenericBizException("Failed to delete article content: " + article.getContentId());
        }
        if (!articleRepo.deleteArticle(articleId)) {
            throw new GenericBizException("Failed to delete article: " + articleId);
        }
    }

}