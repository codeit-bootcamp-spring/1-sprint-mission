package com.sprint.mission.discodeit.global.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinaryContentStorageConfig {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  @Bean
  public Path storageRootPath() {
    return Paths.get(System.getProperty("user.dir"), rootPath);
  }
}
