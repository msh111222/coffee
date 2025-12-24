package com.example.coffee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler(new String[]{"/img/**"}).addResourceLocations(new String[]{"file:/www/img/"}).setCachePeriod(86400);
      registry.addResourceHandler(new String[]{"/fonts/**"}).addResourceLocations(new String[]{"file:/www/fonts/"}).setCachePeriod(86400);
      System.out.println("========== WebConfig 已加载 ==========");
      System.out.println("静态资源映射: /img/** -> file:/www/img/");
      System.out.println("静态资源映射: /fonts/** -> file:/www/fonts/");
   }
}
