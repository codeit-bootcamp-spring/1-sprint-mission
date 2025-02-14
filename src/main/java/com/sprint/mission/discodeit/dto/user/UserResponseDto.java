package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

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
    String userStatus,
    String profilePictureBase64
) {
  public static UserResponseDto from(User user, UserStatus userStatus, BinaryContent profilePicture) {
    return new UserResponseDto(
        user.getUUID(),
        user.getUsername(),
        user.getEmail(),
        user.getNickname(),
        user.getPhoneNumber(),
        user.getCreatedAt(),
        userStatus.getLastOnlineAt(),
        userStatus.getUserStatus().toString(),
        profilePicture != null && profilePicture.getData() != null
            ? Base64.getEncoder().encodeToString(profilePicture.getData())
            : null
    );
  }
}
