package com.sprint.mission.discodeit.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RepositoryProperties {

  //todo - 중복 코드 줄이고 싶음 그러나 configurationProperty로 해결을 못했음
  @Value("${discodeit.repository.type}")
  private String type;

  @Value("${discodeit.repository.base-directory}")
  private String baseDirectory;

  @Value("${discodeit.repository.file-directory}")
  private String fileDirectory;

  @Value("${discodeit.repository.extension}")
  private String extension;

}
