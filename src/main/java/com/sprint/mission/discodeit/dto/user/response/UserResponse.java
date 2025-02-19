package com.sprint.mission.discodeit.dto.user.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//id는 pk, password는 response에서 제외
public record UserResponse(
	UUID id,
	String userid,
	String username,
	String email,
	boolean isOnline,
	Instant createdAt,
	Instant updatedAt) implements Serializable {
}
