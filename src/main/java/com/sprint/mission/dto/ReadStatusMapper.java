package com.sprint.mission.dto;

import com.sprint.mission.dto.response.ReadStatusDto;
import com.sprint.mission.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "channelId", source = "channel.id")
    @Mapping(target = "lastReadAt", source = "lastReadAt")
    ReadStatusDto toDto(ReadStatus readStatus);
}
