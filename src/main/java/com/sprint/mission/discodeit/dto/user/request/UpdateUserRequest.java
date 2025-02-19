package com.sprint.mission.discodeit.dto.user.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequest(
	String userid,
	String username,
	String email,
	MultipartFile profileImage) {
}
