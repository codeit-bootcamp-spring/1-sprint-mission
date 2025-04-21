package com.sprint.mission.discodeit.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {

  public static Properties loadEnv() {
    Properties properties = new Properties();

    try {
      properties.load(new FileInputStream(".env"));
    } catch (IOException e) {
      throw new RuntimeException(".env 파일을 불러오지 못했습니다.", e);
    }

    return properties;
  }
}
