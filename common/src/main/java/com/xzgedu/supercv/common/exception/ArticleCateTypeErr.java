package com.xzgedu.supercv.common.exception;

/**
 * @author qingy
 * @date 2024-12-10
 */
public class ArticleCateTypeErr extends BizException {

    public ArticleCateTypeErr() {
        super(ErrorCode.ARTICLE_CATE_TYPE_ERR);
    }

    public ArticleCateTypeErr(Exception e) {
        super(e, ErrorCode.ARTICLE_CATE_TYPE_ERR);
    }

    public ArticleCateTypeErr(String msg) {
        super(msg, ErrorCode.ARTICLE_CATE_TYPE_ERR);
    }

    public ArticleCateTypeErr(String msg, Exception e) {
        super(msg, e, ErrorCode.ARTICLE_CATE_TYPE_ERR);
    }
}
