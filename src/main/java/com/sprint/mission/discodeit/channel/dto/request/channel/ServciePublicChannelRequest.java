package com.sprint.mission.discodeit.channel.dto.request.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ServciePublicChannelRequest(
	UUID creatorId, //uuid
	String name,
	String description,
	List<UUID> participantIds, //uuid
	Instant createdAt //instant
) {
}
