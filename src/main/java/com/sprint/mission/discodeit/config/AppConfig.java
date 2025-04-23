package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.s3.S3StorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(S3StorageProperties.class)
public class AppConfig {
}