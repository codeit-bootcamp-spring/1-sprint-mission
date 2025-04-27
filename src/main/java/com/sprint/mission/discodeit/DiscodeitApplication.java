package com.sprint.mission.discodeit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.sprint.mission.discodeit.storage.s3.AWSS3Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(AWSS3Properties.class)
public class DiscodeitApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
  }
}
