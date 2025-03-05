package com.sprint.mission.discodeit.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    private static final long MAX_FILE_SIZE_MB = 20; // ✅ 파일 크기 제한 (20MB)
    private static final long MAX_REQUEST_SIZE_MB = 25; // ✅ 전체 요청 크기 제한 (25MB)

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(MAX_FILE_SIZE_MB));
        factory.setMaxRequestSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_MB));
        return factory.createMultipartConfig();
    }
}
