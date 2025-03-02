package com.sprint.mission.discodeit.readStatus.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
	UUID userId,
	UUID channelId,
	Instant lastReadAt
) {

}