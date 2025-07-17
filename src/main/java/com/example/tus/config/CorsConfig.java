package com.example.tus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/files/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("POST", "PATCH", "HEAD", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("Location", "Tus-Resumable", "Upload-Offset", "Upload-Length");
  }
}