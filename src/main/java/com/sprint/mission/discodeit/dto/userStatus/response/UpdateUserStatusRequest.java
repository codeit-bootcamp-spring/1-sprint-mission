package com.sprint.mission.discodeit.dto.userStatus.response;

import java.time.Instant;

public record UpdateUserStatusRequest(
	Instant lastActiveAt
) {
}
