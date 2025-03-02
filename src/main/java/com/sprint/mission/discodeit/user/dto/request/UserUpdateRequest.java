package com.sprint.mission.discodeit.user.dto.request;

public record UserUpdateRequest(
	String newUsername,
	String newEmail,
	String newPassword) {
}
