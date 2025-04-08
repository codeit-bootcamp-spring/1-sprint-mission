package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.BinaryContentDtoMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentDtoMapperImpl;
import com.sprint.mission.discodeit.mapper.MessageDtoMapper;
import com.sprint.mission.discodeit.mapper.MessageDtoMapperImpl;
import com.sprint.mission.discodeit.mapper.UserDtoMapper;
import com.sprint.mission.discodeit.mapper.UserDtoMapperImpl;
import org.springframework.context.annotation.Bean;

public class MapperConfig {

  @Bean
  public UserDtoMapper userDtoMapper() {
    return new UserDtoMapperImpl();
  }

  @Bean
  public BinaryContentDtoMapper binaryContentDtoMapper() {
    return new BinaryContentDtoMapperImpl();
  }

  @Bean
  public MessageDtoMapper messageDtoMapper() {
    return new MessageDtoMapperImpl();
  }
}
