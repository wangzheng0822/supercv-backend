package com.xzgedu.supercv.article.utils;

import com.xzgedu.supercv.common.exception.DataInvalidException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

public class ArticleValidation {
    public static void checkArticleTitle(String title) {
        if (StringUtils.isBlank(title) || title.length() > 60) {
            throw new DataInvalidException(ErrorCode.ARTICLE_TITLE_INVALID);
        }
    }

    public static void checkArticleContent(String content) {
        if (StringUtils.isBlank(content) || content.length() > 30000) {
            throw new DataInvalidException(ErrorCode.ARTICLE_CONTENT_INVALID);
        }
    }
}