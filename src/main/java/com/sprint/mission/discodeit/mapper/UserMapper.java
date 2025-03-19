package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  /*private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;

  public UserDto toDto(User entity) {
    if (entity == null) {
      return null;
    }

    UserDto dto = new UserDto();
    dto.setId(entity.getId());
    dto.setUsername(entity.getUsername());
    dto.setEmail(entity.getEmail());
    dto.setProfile(binaryContentMapper.toDto(entity.getProfile()));
    dto.setOnline(entity.getStatus().isOnline());
    return dto;
  }*/

  @Autowired
  protected BinaryContentMapper binaryContentMapper;

  @Mapping(source = "status.online", target = "online")
  public abstract UserDto toDto(User entity);

  @AfterMapping
  protected void initializeProfile(@MappingTarget UserDto dto, User entity) {
    if (entity.getProfile() != null) {
      Hibernate.initialize(entity.getProfile()); // Lazy 로딩된 필드 강제 초기화
      dto.setProfile(binaryContentMapper.toDto(entity.getProfile()));
    }
  }
}
