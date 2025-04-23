package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3Config {

  private final S3Properties properties;

  private static final long MB = 1024 * 1024;

  @Bean
  public S3AsyncClient s3AsyncClient() {

    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey());

    return S3AsyncClient.crtBuilder()
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .region(Region.of(properties.getRegion()))
        .targetThroughputInGbps(0.1)
        .minimumPartSizeInBytes(8 * MB)
        .build();
  }

  @Bean
  public S3TransferManager transferManager(S3AsyncClient s3AsyncClient) {
    return S3TransferManager.builder()
        .s3Client(s3AsyncClient)
        .build();
  }
}
