package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;

//id는 pk, password는 response에서 제외
public record UserResponse(
	String userid,
	String username,
	String email,
	boolean isOnline,
	Instant createdAt,
	Instant updatedAt) {
}
