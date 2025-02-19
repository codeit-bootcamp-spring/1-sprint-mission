package com.sprint.mission.discodeit.dto.readStatus.request;

import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequest(
	UUID userId,
	UUID channelId,
	UUID messageId,
	Instant lastReadAt) {
}
