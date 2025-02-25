package com.sprint.mission.discodeit.channel.dto.response.channel;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.user.entity.User;

public record ChannelResponse(
	UUID id,
	String name,
	String description,
	Map<UUID, User> participants,
	Instant lastMessageAt,
	List<Message> messageList,
	ChannelType channelType,
	Instant createdAt,
	Instant updatedAt
) {
}
