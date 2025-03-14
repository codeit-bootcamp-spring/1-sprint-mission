package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.mappedDto.UserDto;
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
    public Channel toPublicEntity(PublicChannelCreateDTO request, ChannelType channelType) {
        if ( request == null && channelType == null ) {
            return null;
        }

        Channel.ChannelBuilder channel = Channel.builder();

        if ( request != null ) {
            channel.name( request.name() );
            channel.description( request.description() );
        }
        channel.channelType( channelType );

        return channel.build();
    }

    @Override
    public Channel toPrivateEntity(ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        Channel.ChannelBuilder channel = Channel.builder();

        channel.channelType( channelType );

        return channel.build();
    }
}
