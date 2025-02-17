package com.sprint.mission.discodeit.user.dto.request;

import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequest(
	String userid,
	String username,
	String email,
	MultipartFile profileImage,
	Instant requestAt) {
}
