package com.xzgedu.supercv.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口文档，访问地址：http://localhost:8088/swagger-ui/index.html
 * @author wangzheng
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {

        Components components = new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));

        Info info = new Info().title("SuperCV API").version("v1").description(
                "<br>1. 页面右上角可以选择不同的接口分组（Select a definition）: [user...]" +
                        "<br><br>2. 所有POST请求的参数都应放到Body中，并且Content-Type设置为application/x-www-form-urlencoded" +
                        "<br><br>3. 测试需要登陆之后才能访问的接口，请在页面右上角的[Authorize]处绑定token");

        return new OpenAPI()
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .info(info);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/v1/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("Login")
                .pathsToMatch("/v1/login/**")
                .build();
    }

    @Bean
    public GroupedOpenApi smsCodeApi() {
        return GroupedOpenApi.builder()
                .group("SmsCode")
                .pathsToMatch("/v1/smscode/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
