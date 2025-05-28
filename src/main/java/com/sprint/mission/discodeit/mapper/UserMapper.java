package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  @Autowired
  protected SessionRegistry sessionRegistry;


  @Mapping(target = "online", expression = "java(mapOnline(user))")
  public abstract UserDto toDto(User user);

  protected boolean mapOnline(User user) {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(p -> p instanceof CustomUserDetails)
        .map(p -> (CustomUserDetails) p)
        .anyMatch(details -> ((CustomUserDetails) details).getUser().getId().equals(user.getId()));
  }
}
