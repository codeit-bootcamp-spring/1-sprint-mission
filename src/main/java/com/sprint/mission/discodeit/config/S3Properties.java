package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discodeit.storage.s3")
public class S3Properties {

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private int presignedUrlExpiration;
}
