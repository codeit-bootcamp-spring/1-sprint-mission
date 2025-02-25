package com.sprint.mission.discodeit.channel.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.channel.dto.response.channel.ChannelResponse;
import com.sprint.mission.discodeit.channel.dto.response.channel.PrivateChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;

@Component
public class ChannelMapper {

	public ChannelResponse channelToChannelResponse(Channel channel) {
		return new ChannelResponse(
			channel.getId(),
			channel.getName(),
			channel.getDescription(),
			channel.getParticipants(),
			channel.getLastMessageAt(),
			channel.getMessageList(),
			channel.getChannelType(),
			channel.getCreatedAt(),
			channel.getUpdatedAt()
		);
	}

	public PrivateChannelResponse channelToPrivateChannelResponse(Channel channel) {
		return new PrivateChannelResponse(
			channel.getId(),
			channel.getParticipants(),
			channel.getLastMessageAt(),
			channel.getMessageList(),
			channel.getChannelType(),
			channel.getCreatedAt(),
			channel.getUpdatedAt()
		);
	}

	public List<ChannelResponse> channelListToChannelResponseList(List<Channel> channels) {

		List<ChannelResponse> responses = new ArrayList<>();
		for (Channel channel : channels) {
			responses.add(channelToChannelResponse(channel));
		}
		return responses;
	}
}
