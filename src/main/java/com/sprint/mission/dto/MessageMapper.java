package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.MessageDto;
import com.sprint.mission.entity.main.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDto toDto(Message message);
}
