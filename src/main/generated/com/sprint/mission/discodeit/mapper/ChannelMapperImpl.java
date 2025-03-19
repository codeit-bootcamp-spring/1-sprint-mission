package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-13T10:34:21+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public ChannelDto toDto(Channel channel, UserMapper userMapper) {
        if ( channel == null ) {
            return null;
        }

        ChannelDto channelDto = new ChannelDto();

        channelDto.setId( channel.getId() );
        channelDto.setType( channel.getType() );
        channelDto.setName( channel.getName() );
        channelDto.setDescription( channel.getDescription() );

        channelDto.setParticipants( getParticipants(channel, userMapper) );
        channelDto.setLastMessageAt( getLastMessageTime(channel) );

        return channelDto;
    }
}
