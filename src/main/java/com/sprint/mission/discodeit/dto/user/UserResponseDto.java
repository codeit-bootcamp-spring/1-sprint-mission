package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;

public record UserResponseDto(
    String userId,
    String username,
    String email,
    String nickname,
    String phoneNumber,
    Instant createdAt,
    Instant lastOnlineAt,
    String description,
    String userStatus,
    String profilePictureBase64
) {
}
