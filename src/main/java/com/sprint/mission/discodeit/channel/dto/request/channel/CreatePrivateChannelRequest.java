package com.sprint.mission.discodeit.channel.dto.request.channel;

import java.time.Instant;
import java.util.UUID;

public record CreatePrivateChannelRequest(
	UUID authorId,
	UUID receiverId,
	Instant createdAt
) {
}
