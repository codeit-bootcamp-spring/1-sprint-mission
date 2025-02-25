package com.sprint.mission.discodeit.channel.dto.request.readStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequest(
	UUID userId,
	UUID channelId,
	UUID messageId,
	Instant lastReadAt) {
}
