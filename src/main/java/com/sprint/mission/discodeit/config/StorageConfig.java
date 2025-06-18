package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.local.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(S3StorageProperties.class)
public class StorageConfig {

  @Bean
  @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
  public BinaryContentStorage s3BinaryContentStorage(S3StorageProperties props) {
    return new S3BinaryContentStorage(
        props.accessKey(),
        props.secretKey(),
        props.region(),
        props.bucket(),
        props.presignedUrlExpiration()
    );
  }

//  @Bean
//  @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
//  public BinaryContentStorage localBinaryContentStorage() {
//    return new LocalBinaryContentStorage();
//  }
}
