package com.sprint.mission.discodeit.dto.user.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateUserRequest(
	String userid,
	String password,
	String username,
	String email,
	MultipartFile profileImage) {
}
