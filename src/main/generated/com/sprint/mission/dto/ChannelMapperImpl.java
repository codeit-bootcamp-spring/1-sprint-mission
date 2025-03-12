package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ChannelDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

/**
* <h2>ChannelMapper</h2>
* 채널 관련 DTO 변환을 위한 Mapper입니다.
* 채널 조회 시 사용되는 DTO 변환을 담당합니다.
*
*/
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
}
