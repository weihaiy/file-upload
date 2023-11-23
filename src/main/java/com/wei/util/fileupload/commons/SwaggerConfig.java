package com.wei.util.fileupload.commons;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("文件上传接口文档")
                        .description("")
                        .version("1.0.0"))
                .externalDocs(new ExternalDocumentation().description("")
                        .url(""));
    }
    @Bean
    public GroupedOpenApi Main() {
        return GroupedOpenApi.builder()
                //分组名
                .group("main")
                //扫描路径，将路径下有swagger注解的接口解析到文档中
                .packagesToScan("com.wei.util.fileupload.controller")
                .build();
    }
}
