package com.sprint.mission.discodeit.auth.dto;

public record LoginUserRequest(
	String userId,
	String password
) {
}
