package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.local.LocalBinaryContentStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class StorageConfiguration {

  @Bean
  public BinaryContentStorage binaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    return new LocalBinaryContentStorage(rootPath);
  }
}
