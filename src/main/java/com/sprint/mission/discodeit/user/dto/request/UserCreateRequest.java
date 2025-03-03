package com.sprint.mission.discodeit.user.dto.request;

public record UserCreateRequest(
	String username,
	String email,
	String password) {
}
