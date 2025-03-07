package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

    public ChannelDto toDto(Channel channel) {
        if (channel == null) {
            return null;
        }

        return ChannelDto.builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .type(channel.getType().toString())
                .build();
    }

    public Channel toEntity(ChannelDto dto) {
        if(dto == null) {
            return null;
        }

        return Channel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .type(ChannelType.valueOf(dto.getType()))
                .build();
    }
}
