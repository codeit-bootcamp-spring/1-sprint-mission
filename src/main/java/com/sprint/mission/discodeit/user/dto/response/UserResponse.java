package com.sprint.mission.discodeit.user.dto.response;

import java.time.Instant;
import java.util.UUID;

//id는 pk, password는 response에서 제외
public record UserResponse(
	UUID id,
	Instant createdAt,
	Instant updatedAt,
	String username,
	String email,
	UUID profileId,
	Boolean online) {
}
