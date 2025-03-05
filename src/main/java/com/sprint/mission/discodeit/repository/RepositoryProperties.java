package com.sprint.mission.discodeit.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
@ConfigurationProperties(prefix = "discodeit.repository")
public class RepositoryProperties {
    private String fileDirectory;
    private String type;
}
