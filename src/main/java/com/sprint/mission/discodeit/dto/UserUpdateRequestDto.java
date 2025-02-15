package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserUpdateRequestDto(
        UUID userId,
        String name,
        String email,
        String password,
        byte[] binaryContentData
) {
}
