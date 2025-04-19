package com.sprint.mission.dto;

import com.sprint.mission.dto.response.UserStatusDto;
import com.sprint.mission.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

import static org.mapstruct.MappingInheritanceStrategy.*;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatusDto toDto(UserStatus userStatus);
}
