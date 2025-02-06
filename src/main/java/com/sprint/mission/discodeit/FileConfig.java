package com.sprint.mission.discodeit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileConfig {
    private String binaryContentJsonPath;
    private String channelJsonPath;
    private String messageJsonPath;
    private String readStatusJsonPath;
    private String userJsonPath;
    private String userStatusJsonPath;
}
