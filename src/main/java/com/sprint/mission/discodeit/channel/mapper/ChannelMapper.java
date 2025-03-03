package com.sprint.mission.discodeit.channel.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sprint.mission.discodeit.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
	ChannelResponse toDto(Channel channel);

	List<ChannelResponse> toDtoList(List<Channel> channels);
}
