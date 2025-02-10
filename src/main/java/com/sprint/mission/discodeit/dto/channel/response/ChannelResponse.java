package com.sprint.mission.discodeit.dto.channel.response;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelResponse(
	UUID id,
	String name,
	String description,
	Map<UUID, UserResponse> participants,
	Instant lastMessageAt,
	ChannelType channelType,
	Instant createdAt,
	Instant updatedAt
) {
}
