package com.sprint.mission.discodeit.config;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  public Path getRoot() {
    if (rootPath == null) {
      throw new IllegalStateException("Root path is not set");
    }
    return Path.of(rootPath);
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }
}
