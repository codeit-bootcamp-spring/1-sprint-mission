package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.ToString;

import java.time.Instant;
import java.util.Base64;


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
