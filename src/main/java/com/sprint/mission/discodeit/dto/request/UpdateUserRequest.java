package com.sprint.mission.discodeit.dto.request;

public record UpdateUserRequest(
	String userid,
	String username,
	String email,
	byte[] profileImage,
	String contentType,
	String fileName) {
}
