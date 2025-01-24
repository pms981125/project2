package com.lec.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 파일을 제공할 경로와 URL 매핑
    	registry.addResourceHandler("/uploads/**")
        		.addResourceLocations("file:/D:/sungil/98.temp/upload/");

    }
}
