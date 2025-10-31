package com.project.livingauction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        "http://living-auction.com",
                        "https://living-auction.com",
                        "https://living-auction.vercel.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}
