package com.sprint.mission.discodeit.dto.userDto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequestDto(String email, String password, String name, String nickname, String phoneNumber, MultipartFile profileImageFile) {
}
