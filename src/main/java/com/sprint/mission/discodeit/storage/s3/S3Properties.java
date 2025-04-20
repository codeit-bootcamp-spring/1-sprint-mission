package com.sprint.mission.discodeit.storage.s3;

import jakarta.annotation.PostConstruct;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Properties {

  private final Environment environment;
  private final Properties properties = new Properties();

  @PostConstruct
  public void init() {
    properties.setProperty("AWS_S3_ACCESS_KEY",
        environment.getProperty("discodeit.storage.s3.access-key", ""));

    properties.setProperty("AWS_S3_SECRET_KEY",
        environment.getProperty("discodeit.storage.s3.secret-key", ""));

    properties.setProperty("AWS_S3_REGION",
        environment.getProperty("discodeit.storage.s3.region", ""));

    properties.setProperty("AWS_S3_BUCKET",
        environment.getProperty("discodeit.storage.s3.bucket", ""));
  }

  public String getAccessKey() {
    return properties.getProperty("AWS_S3_ACCESS_KEY");
  }

  public String getSecretKey() {
    return properties.getProperty("AWS_S3_SECRET_KEY");
  }

  public String getRegion() {
    return properties.getProperty("AWS_S3_REGION");
  }

  public String getBucket() {
    return properties.getProperty("AWS_S3_BUCKET");
  }
}