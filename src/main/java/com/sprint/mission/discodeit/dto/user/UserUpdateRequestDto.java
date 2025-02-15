package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateRequestDto(
        UUID userId,
        String name,
        String email,
        String password,
        byte[] binaryContentData
) {
}
