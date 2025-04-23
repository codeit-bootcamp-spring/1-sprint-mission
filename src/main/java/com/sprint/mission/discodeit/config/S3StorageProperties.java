package com.sprint.mission.discodeit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("discodeit.storage.s3")
public class S3StorageProperties {
  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private int presignedUrlExpiration;
}
