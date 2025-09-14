package com.stripe.getways.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("*") // Allow all origins (or specify: "http://localhost:5173")
                        .allowedMethods("*") // Allow all HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false)
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}

