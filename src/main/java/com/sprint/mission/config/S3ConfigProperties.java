package com.sprint.mission.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "discodeit.storage.s3")
@Validated
public record S3ConfigProperties(
    @NotBlank(message = "Access key is required")
    String accessKey,
    @NotBlank(message = "Secret key is required")
    String secretKey,
    @NotBlank(message = "Region is required")
    String region,
    @NotBlank(message = "Bucket is required")
    String bucket,
    @NotNull(message = "Presigned-url-expiration is required")
    long presignedUrlExpiration) {

}
