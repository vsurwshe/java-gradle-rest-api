package com.rvtech.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rvtech.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(
                        new ApiInfoBuilder()
                        .title("Apache Shiro &amp; Swagger")
                        .description("use Apache Shiro take over Swagger Certification and authorization")
                        .version("1.0.0")
                        .contact(
                                new Contact("Fox under the tree",
                                        "https://www.ramostear.com",
                                        "ramostear@163.com")
                        )
                        .license("The Apache License")
                        .build()
                );
    }
}