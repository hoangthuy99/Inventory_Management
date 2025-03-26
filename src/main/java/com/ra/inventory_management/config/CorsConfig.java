package com.ra.inventory_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Value("${path-upload}")
    private String uploadDir;

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins) // Hoặc "*" nếu test
                        .allowedMethods(allowedMethods)
                        .allowedHeaders(allowedHeaders)
                        .allowCredentials(allowCredentials);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                System.out.println("Upload Directory: " + uploadDir);
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadDir + "/");
            }
        };
    }
}