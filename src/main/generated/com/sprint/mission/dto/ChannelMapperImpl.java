package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public ChannelDto toDto(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        ChannelType channelType = null;
        String name = null;
        String description = null;

        id = channel.getId();
        channelType = channel.getChannelType();
        name = channel.getName();
        description = channel.getDescription();

        List<UserDto> participants = null;
        Instant lastMessageAt = null;

        ChannelDto channelDto = new ChannelDto( id, channelType, name, description, participants, lastMessageAt );

        return channelDto;
    }

    @Override
    public Channel update(ChannelDtoForUpdate dto, Channel channel) {
        if ( dto == null ) {
            return channel;
        }

        channel.setName( dto.name() );
        channel.setDescription( dto.description() );

        return channel;
    }

    @Override
    public Channel toPublicEntity(PublicChannelCreateDTO request) {
        if ( request == null ) {
            return null;
        }

        Channel channel = toPrivateEntity();

        channel.setName( request.name() );
        channel.setDescription( request.description() );

        return channel;
    }
}
