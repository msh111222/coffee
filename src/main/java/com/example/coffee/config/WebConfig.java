package com.example.coffee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /img/ 路径映射到服务器的 /www/img/ 文件夹
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/www/img/")
                .setCachePeriod(86400); // 缓存 1 天
        
        System.out.println("========== WebConfig 已加载 ==========");
        System.out.println("静态资源映射: /img/** -> file:/www/img/");
    }
}

