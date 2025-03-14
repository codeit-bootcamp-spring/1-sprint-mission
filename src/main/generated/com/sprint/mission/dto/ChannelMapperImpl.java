package com.sprint.mission.dto;

import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.dto.response.UserDto;
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
    public Channel toPublicEntity(PublicChannelCreateDTO request, ChannelType channelType) {
        if ( request == null && channelType == null ) {
            return null;
        }

        String name = null;
        String description = null;
        if ( request != null ) {
            name = request.name();
            description = request.description();
        }
        ChannelType channelType1 = null;
        channelType1 = channelType;

        Channel channel = new Channel( name, description, channelType1 );

        return channel;
    }

    @Override
    public Channel toPrivateEntity(ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        ChannelType channelType1 = null;

        channelType1 = channelType;

        String name = null;
        String description = null;

        Channel channel = new Channel( name, description, channelType1 );

        return channel;
    }
}
