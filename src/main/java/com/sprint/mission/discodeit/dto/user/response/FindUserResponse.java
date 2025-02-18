package com.sprint.mission.discodeit.dto.user.response;

import org.springframework.web.multipart.MultipartFile;

public record FindUserResponse(
        String name,
        String nickname,
        String email,
        MultipartFile profileImage,
        boolean isOnline
) {
}
