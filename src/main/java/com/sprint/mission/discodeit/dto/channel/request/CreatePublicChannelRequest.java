package com.sprint.mission.discodeit.dto.channel.request;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreatePublicChannelRequest(
	UUID creatorId,
	String name,
	String description,
	List<UUID> participantIds,
	Instant createdAt
) {
}
