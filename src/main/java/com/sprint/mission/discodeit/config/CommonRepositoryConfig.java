package com.sprint.mission.discodeit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class CommonRepositoryConfig {

    @Value("${discodeit.repository.type:jcf}")
    private String repositoryType;

    @Value("${discodeit.repository.file-directory:temp}")
    private String baseDirectory;

    private Path getFileStoragePath(String subPath) {
        if ("file".equalsIgnoreCase(repositoryType)) {  // 대소문자 구분 없이 비교
            return Paths.get(System.getProperty("user.dir"), "temp", subPath);
        } else {
            return Paths.get(System.getProperty("user.dir"), baseDirectory, subPath);
        }
    }

    @Bean
    public Path fileUserStoragePath() {
        return getFileStoragePath("user");
    }

    @Bean
    public Path fileChannelStoragePath() {
        return getFileStoragePath("channel");
    }

    @Bean
    public Path fileMessageStoragePath() {
        return getFileStoragePath("message");
    }

    @Bean
    public Path fileBinaryContentStoragePath() {
        return getFileStoragePath("binaryContent");
    }

    @Bean
    public Path fileReadStatusStoragePath() {
        return getFileStoragePath("readStatus");
    }

    @Bean
    public Path fileUserStatusStoragePath() {
        return getFileStoragePath("userStatus");
    }
}
