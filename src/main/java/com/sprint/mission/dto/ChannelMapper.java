package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import org.mapstruct.*;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);

    //@Mapping(target = ".", expression = "java(Channel.createChannel(request.name, request.description, PUBLIC))")
    Channel toPublicEntity(PublicChannelCreateDTO request, ChannelType channelType);

    Channel toPrivateEntity(ChannelType channelType);
}
