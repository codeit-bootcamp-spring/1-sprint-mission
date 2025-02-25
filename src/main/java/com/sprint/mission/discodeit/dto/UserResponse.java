package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        UserStatus.Status status,
        UUID profileId
) {
    public static UserResponse entityToDto(User user, UserStatus.Status userStatus, UUID binaryContentId) {
        return UserResponse.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .name(user.getName())
                .email(user.getEmail())
                .status(userStatus)
                .profileId(binaryContentId)
                .build();
    }
}
