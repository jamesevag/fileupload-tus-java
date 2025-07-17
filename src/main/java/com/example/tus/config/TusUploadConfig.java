package com.example.tus.config;

import me.desair.tus.server.TusFileUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TusUploadConfig {

  @Bean
  public TusFileUploadService tusFileUploadService() {
    return new TusFileUploadService()
        .withStoragePath("/tmp/tus/uploads")
        .withUploadURI("/files"); // This will be your TUS endpoint
  }
}