package com.xzgedu.supercv.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter拦截器的配置类
 * @author wangzheng
 */
@Configuration
public class FilterConfig {

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public FilterRegistrationBean addCorsFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(corsFilter);
        bean.addUrlPatterns("/v1/*");
        bean.addUrlPatterns("/admin/*");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean addLogTraceIdFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new LogTraceIdFilter());
        bean.addUrlPatterns("/v1/*");
        bean.addUrlPatterns("/admin/*");
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public FilterRegistrationBean addAccessLogFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new AccessLogFilter());
        bean.addUrlPatterns("/v1/*");
        bean.addUrlPatterns("/admin/*");
        bean.setOrder(3);
        return bean;
    }
}