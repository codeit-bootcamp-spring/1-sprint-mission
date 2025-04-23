package com.sprint.mission.discodeit.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@TestConfiguration
class TestConfig {

  @Bean
  @Primary
  public S3AsyncClient s3AsyncClient() {
    return Mockito.mock(S3AsyncClient.class);
  }
}
