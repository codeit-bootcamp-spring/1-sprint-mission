package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.entity.main.ChannelType.PRIVATE;
import static com.sprint.mission.entity.main.ChannelType.PUBLIC;
import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);


    Channel update(ChannelDtoForUpdate dto, @MappingTarget Channel channel);

    @ValueMapping(target = "channelType", source = "PUBLIC")
    Channel toPublicEntity(PublicChannelCreateDTO request);

    //@ValueMapping(target = "channelType", source = "PRIVATE")
    default Channel toPrivateEntity(){
        Channel privateChannel = new Channel();
        privateChannel.setChannelType(PRIVATE);
        return privateChannel;
    };
}
