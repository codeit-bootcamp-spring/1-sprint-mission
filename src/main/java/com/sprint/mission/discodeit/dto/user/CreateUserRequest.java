package com.sprint.mission.discodeit.dto.user;

import org.springframework.web.multipart.MultipartFile;

public record CreateUserRequest(
    String username,
    String password,
    String email,
    String nickname,
    String phoneNumber,
    MultipartFile profileImage,
    String description
) {

}
