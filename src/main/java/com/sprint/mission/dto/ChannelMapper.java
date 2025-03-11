package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.entity.main.Channel;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);

}
