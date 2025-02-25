package com.sprint.mission.discodeit.user.dto.request;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusRequest(
	UUID userId,
	Instant lastActiveAt
) {
}
