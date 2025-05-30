package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  @Autowired
  protected BinaryContentMapper binaryContentMapper;

  @Mapping(source = "profile", target = "profile")
  @Mapping(target = "online", ignore = true)
  public abstract UserResponse toDto(User user);

  @Mapping(source = "user.profile", target = "profile")
  @Mapping(target = "online", expression = "java(online)")
  public abstract UserResponse toDto(User user, boolean online);

  protected BinaryContentResponse map(Optional<BinaryContent> optionalProfile) {
    return optionalProfile.map(binaryContentMapper::toDto).orElse(null);
  }

}
