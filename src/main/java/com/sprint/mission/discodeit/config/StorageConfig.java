package com.sprint.mission.discodeit.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local")
@Slf4j
public class StorageConfig {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPathString;

  private Path rootPath;

  public Path getRootPath() {
    if (rootPath == null) {
      rootPath = Paths.get(rootPathString);
    }
    log.info("root path: {}", rootPath);
    return rootPath;
  }

}
