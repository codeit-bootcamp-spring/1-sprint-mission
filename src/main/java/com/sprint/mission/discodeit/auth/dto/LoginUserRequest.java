package com.sprint.mission.discodeit.auth.dto;

public record LoginUserRequest(
	String username,
	String password
) {
}
