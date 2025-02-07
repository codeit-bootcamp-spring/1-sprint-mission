package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;

@Builder
public record CreateUserRequest(String userId, String password, String username, String eamil,
								MultipartFile profileImage) {
}
