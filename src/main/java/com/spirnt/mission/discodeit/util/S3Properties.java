package com.spirnt.mission.discodeit.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "discodeit.s3")
public class S3Properties {

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private String presignedUrlExpiration;

}