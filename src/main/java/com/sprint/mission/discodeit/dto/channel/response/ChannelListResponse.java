package com.sprint.mission.discodeit.dto.channel.response;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

//ChannelResponse와 받는건 똑같으니까 삭제하는게 좋겠다.
public record ChannelListResponse(
	UUID id,
	String name,
	String description,
	Map<UUID, UserResponse> participants,
	Instant lastMessageAt,
	ChannelType channelType,
	Instant createdAt,
	Instant updatedAt
) {
	public static ChannelListResponse from(Channel channel) {
		return new ChannelListResponse(
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
