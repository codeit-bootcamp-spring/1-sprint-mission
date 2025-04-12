package com.sprint.mission.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import static com.sprint.mission.config.S3Config.*;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@EnableConfigurationProperties(S3ConfigProperties.class)
@RequiredArgsConstructor
public class S3Config {

    private final S3ConfigProperties properties;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());
        return S3Client.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @ConfigurationProperties(prefix = "discodeit.storage.s3")
    public static class S3ConfigProperties {
        @NotBlank (message = "Access key is required")
        private String accessKey;
        @NotBlank (message = "Secret key is required")
        private String secretKey;
        @NotBlank (message = "Region is required")
        private String region;
        @NotBlank (message = "Bucket is required")
        private String bucket;
        @NotBlank (message = "Presigned-url-expiration is required")
        private long presignedUrlExpiration;
    }
}
