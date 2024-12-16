package com.xzgedu.supercv.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor拦截器配置类
 * @author wangzheng
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthTokenInterceptor authTokenInterceptor;

    @Autowired
    private IdentifierInterceptor identifierInterceptor;

    @Autowired
    private AdminPassInterceptor adminPassInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    ResumeOwnershipInterceptor resumeOwnershipInterceptor;

    @Autowired
    VipBenefitInterceptor vipBenefitInterceptor;

    @Autowired
    ArticlePermissionInterceptor articlePermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //验证是否登陆
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/v1/**", "/admin/**")
                .excludePathPatterns("/v1/login/**")
                .excludePathPatterns("/v1/product/**")
                .excludePathPatterns("/v1/resume/template/list")
                .excludePathPatterns("/v1/resume/template/list/mock")
                .excludePathPatterns("/v1/article/list")
                .order(1);

        //非管理员用户只能操作自己的数据，参数uid要跟header中uid相同
        registry.addInterceptor(identifierInterceptor)
                .addPathPatterns("/v1/**", "/admin/**")
                .excludePathPatterns("/v1/login/**")
                .order(2);

        //管理员接口访问权限控制
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/account/check")
                .order(3);

        //管理员可以访问所有接口
        registry.addInterceptor(adminPassInterceptor)
                .addPathPatterns("/v1/**")
                .order(4);

        //简历归属权检查
        registry.addInterceptor(resumeOwnershipInterceptor)
                .addPathPatterns("/v1/resume/detail")
                .addPathPatterns("/v1/resume/update")
                .addPathPatterns("/v1/resume/delete")
                .addPathPatterns("/v1/resume/copy")
                .addPathPatterns("/v1/resume/baseinfo/**")
                .addPathPatterns("/v1/resume/module/**")
                .order(5);

        //仅VIP可操作的接口
        registry.addInterceptor(vipBenefitInterceptor)
                .addPathPatterns("/v1/resume/create")
                .addPathPatterns("/v1/resume/create-from-file")
                .addPathPatterns("/v1/resume/copy")
                .addPathPatterns("/v1/resume/update")
                .addPathPatterns("/v1/resume/baseinfo/**")
                .addPathPatterns("/v1/resume/module/**")
                .order(6);

        //文章详情权限检查
        registry.addInterceptor(articlePermissionInterceptor)
                .addPathPatterns("/v1/article/detail")
                .order(7);
    }
}