package com.sprint.mission.discodeit.user.dto.request;

import java.time.Instant;

public record UserStatusUpdateRequest(
	Instant newLastActiveAt
) {
}
