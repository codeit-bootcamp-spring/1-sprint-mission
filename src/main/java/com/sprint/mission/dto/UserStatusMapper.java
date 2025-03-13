package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.UserStatusDto;
import com.sprint.mission.entity.addOn.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    UserStatusDto toDto(UserStatus userStatus);
}
