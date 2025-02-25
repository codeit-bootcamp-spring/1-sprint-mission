package com.sprint.mission.discodeit.user.dto.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.UserStatusType;

//id는 pk, password는 response에서 제외
public record UserResponse(
	UUID id,
	String userid,
	String username,
	String email,
	UserStatusType userStatus,
	UUID profileImageId,
	Instant createdAt,
	Instant updatedAt) implements Serializable {
}
