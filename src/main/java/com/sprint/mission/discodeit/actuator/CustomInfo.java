package com.sprint.mission.discodeit.actuator;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfo implements InfoContributor {

  @Value("${discodeit.storage.type}")
  private String storageType;

  @Value("${discodeit.storage.local.root-path}")
  private String storagePath;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String ddlAuto;

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  @Value("${spring.servlet.multipart.max-file-size}")
  private String maxFileSize;

  @Value("${spring.servlet.multipart.max-request-size}")
  private String maxRequestSize;


  @Override
  public void contribute(Builder builder) {
    builder.withDetail("app", Map.of(
        "name", "Discodeit",
        "version", "1.7.0"
    ));
    builder.withDetail("java", Map.of(
        "version", "17"
    ));
    builder.withDetail("spring-boot", Map.of(
        "version", "3.4.0"
    ));
    builder.withDetail("datasource", Map.of(
        "url", url,  // 직접 바인딩 가능
        "driver", driver
    ));
    builder.withDetail("jpa", Map.of(
        "ddl-auto", ddlAuto
    ));
    builder.withDetail("storage", Map.of(
        "type", storageType,
        "path", storagePath
    ));
    builder.withDetail("multipart", Map.of(
        "max-file-size", maxFileSize,
        "max-request-size", maxRequestSize
    ));
  }
}
