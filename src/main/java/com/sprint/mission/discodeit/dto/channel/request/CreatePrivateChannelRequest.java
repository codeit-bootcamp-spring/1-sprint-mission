package com.sprint.mission.discodeit.dto.channel.request;

import java.time.Instant;
import java.util.UUID;

public record CreatePrivateChannelRequest(
	UUID authorId,
	UUID receiverId,
	Instant createdAt
) {
}
