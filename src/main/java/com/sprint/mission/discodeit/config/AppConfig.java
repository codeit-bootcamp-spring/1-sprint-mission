package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
  public BinaryContentStorage binaryContentStorage() {
    return new LocalBinaryContentStorage();
  }
}
