package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = BinaryContentDtoMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoMapper {

  @Mapping(source = "profile", target = "profile")
  UserDto toDto(User user);
}
