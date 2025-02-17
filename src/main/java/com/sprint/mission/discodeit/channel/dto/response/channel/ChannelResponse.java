package com.sprint.mission.discodeit.channel.dto.response.channel;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.user.entity.User;

public record ChannelResponse(
	UUID id,
	String name,
	String description,
	Map<UUID, User> participants,
	Instant lastMessageAt,
	ChannelType channelType,
	Instant createdAt,
	Instant updatedAt
) {
	public static ChannelResponse from(Channel channel) {
		return new ChannelResponse(
			channel.getId(),
			channel.getName(),
			channel.getDescription(),
			channel.getParticipants(),
			channel.getLastMessageAt(),
			channel.getChannelType(),
			channel.getCreatedAt(),
			channel.getUpdatedAt()
		);
	}
}
