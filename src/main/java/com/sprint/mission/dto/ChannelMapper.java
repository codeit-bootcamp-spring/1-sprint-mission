package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import org.mapstruct.*;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);


    Channel update(ChannelDtoForUpdate dto, @MappingTarget Channel channel);

    @Mapping(target = "channelType", constant = "PUBLIC")
    Channel toPublicEntity(PublicChannelCreateDTO request);

    //@ValueMapping(target = "channelType", source = "PRIVATE")
    @Mapping(target = "channelType", source = "type")
    Channel toPrivateEntity(ChannelType type);
//    default Channel toPrivateEntity(){
//        Channel privateChannel = new Channel();
//        privateChannel.setChannelType(PRIVATE);
//        return privateChannel;
//    };
}
