package com.sprint.mission.discodeit.dto.request;

public record CreateUserRequest(
	String userid,
	String password,
	String username,
	String email,
	byte[] profileImage,
	String contentType,
	String fileName) {
}
