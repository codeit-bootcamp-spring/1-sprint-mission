package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AWSTestConfig {
  private static final Properties props = new Properties();
  static {
    try (FileInputStream fis = new FileInputStream(".env")) {
      props.load(fis);
    } catch (IOException e) {
      throw new RuntimeException(".env 파일 로드 실패", e);
    }
  }

  public static String get(String key) {
    String v = props.getProperty(key);
    if (v == null) throw new IllegalStateException(key + " 값이 .env에 없습니다");
    return v;
  }
}
