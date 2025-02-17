package com.sprint.mission.discodeit.dto.auth.request;

public record LoginUserRequest(
	String userId,
	String password
) {
}
