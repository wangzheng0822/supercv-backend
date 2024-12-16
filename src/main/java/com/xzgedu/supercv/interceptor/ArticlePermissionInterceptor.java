package com.xzgedu.supercv.interceptor;

import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.vip.service.VipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ArticlePermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private VipService vipService;

    @Autowired
    private ArticleService articleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Boolean isAdmin = (Boolean) request.getAttribute("admin");
        if (isAdmin != null && isAdmin == true) return true;

        Long uid = InterceptorUtils.parseLong(request.getHeader("uid"));
        boolean permit = uid != null && vipService.permitValidVip(uid);
        if (permit) return true;

        Long articleId = InterceptorUtils.parseLong(request.getParameter("article_id"));
        if (articleId != null) {
            Article article = articleService.getArticleDetailById(articleId);
            if (article != null && article.isFree()) {
                return true;
            }
        }

        log.warn("No Permission for non-vip user: [uid={}]", uid);
        InterceptorUtils.writeResponse(response, ErrorCode.NO_PERMISSION);
        return false;
    }
}