package com.xzgedu.supercv.common.exception;

/**
 * @author qingy
 * @date 2024-12-11
 */
public class ArticleContentErr extends BizException {

        public ArticleContentErr() {
            super(ErrorCode.ARTICLE_CONTENT_ERR);
        }

        public ArticleContentErr(Exception e) {
            super(e, ErrorCode.ARTICLE_CONTENT_ERR);
        }

        public ArticleContentErr(String msg) {
            super(msg, ErrorCode.ARTICLE_CONTENT_ERR);
        }

        public ArticleContentErr(String msg, Exception e) {
            super(msg, e, ErrorCode.ARTICLE_CONTENT_ERR);
        }
}
