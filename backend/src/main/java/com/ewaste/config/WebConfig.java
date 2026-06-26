package com.ewaste.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map uploads URL to local directories
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:../uploads/", "file:./uploads/");

        // Map qr URL to local directories
        registry.addResourceHandler("/qr/**")
                .addResourceLocations("file:../qr/", "file:./qr/");
    }
}
