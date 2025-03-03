package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discodeit.repository")
@Getter
@Setter
public class FileConfig {

  private String type;
  private String fileDirectory = ".discodeit";

  private String binaryContentJsonPath;
  private String channelJsonPath;
  private String messageJsonPath;
  private String readStatusJsonPath;
  private String userJsonPath;
  private String userStatusJsonPath;
}


