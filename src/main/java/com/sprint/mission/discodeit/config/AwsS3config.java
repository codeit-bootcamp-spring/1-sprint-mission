package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "s3"
)
public class AwsS3config {

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("{cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.region.static")
  private String region;

  @Value("${cloud.aws.region.static")
  private String bucket;

  @Bean
  public S3BinaryContentStorage s3BinaryContentStorage() {
    return new S3BinaryContentStorage(accessKey, secretKey, region, bucket);
  }
}
