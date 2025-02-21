package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        UserStatus.Status status
) {
    public static UserResponse entityToDto(User user, UserStatus userStatus) {
        return new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                userStatus.getStatus()
        );
    }
}
