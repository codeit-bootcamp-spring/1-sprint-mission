package com.sprint.mission.discodeit.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateUserRequest(
	String userid,
	String password,
	String username,
	String email,
	MultipartFile profileImage) {
}
