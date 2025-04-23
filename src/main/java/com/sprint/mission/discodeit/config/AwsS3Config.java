package com.sprint.mission.discodeit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

  private final S3StorageProperties properties;

  @Bean
  public S3Client s3Client() {
    AwsCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),properties.getSecretKey());

    return S3Client.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(() -> credentials)
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    AwsCredentials credentials = AwsBasicCredentials.create(properties.getAccessKey(),properties.getSecretKey());
    return S3Presigner.builder()
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(() -> credentials)
        .build();
  }
}
