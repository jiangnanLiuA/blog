package com.jiangnan.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()

                //只监控com.jiangnan.controller包的api
                .apis(RequestHandlerSelectors.basePackage("com.jiangnan.controller"))

                // 对所有api进行监控
                .apis(RequestHandlerSelectors.any())

                //不显示错误的接口地址，也就是错误路径不监控
                .paths(Predicates.not(PathSelectors.regex("/error.*")))

                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("jiangnan", "https://jiangnan.cn", "jiangnan@jn.com");
        return new ApiInfoBuilder()
                .title("文档标题")
                .description("文档描述")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}
