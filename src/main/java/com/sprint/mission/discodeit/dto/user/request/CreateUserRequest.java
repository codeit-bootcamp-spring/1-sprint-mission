package com.sprint.mission.discodeit.dto.user.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateUserRequest(
        String name,
        String nickname,
        String email,
        String password
) {
}
