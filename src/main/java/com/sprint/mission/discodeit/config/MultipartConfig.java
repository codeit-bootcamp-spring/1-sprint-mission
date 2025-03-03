package com.sprint.mission.discodeit.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();

    factory.setMaxFileSize(DataSize.ofMegabytes(10));  // 최대 10MB 파일
    factory.setMaxRequestSize(DataSize.ofMegabytes(20));  // 전체 요청 최대 20MB

    return factory.createMultipartConfig();
  }
}
