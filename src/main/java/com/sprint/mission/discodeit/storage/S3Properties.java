package com.sprint.mission.discodeit.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.storage.s3")
@Getter
@Setter
public class S3Properties {
  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
}
