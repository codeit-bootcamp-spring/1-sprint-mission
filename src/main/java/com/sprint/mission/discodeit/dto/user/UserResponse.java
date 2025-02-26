package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        boolean isCurrentlyLoggedIn,
        Instant createdAt,
        UUID profileImageId

) {
    public static UserResponse from(User user, UserStatus status) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                status.isCurrentlyLoggedIn(),
                user.getCreatedAt(),
                user.getProfileId()
        );
    }
}