package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDetailResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        boolean isOnline
) {
    public static UserDetailResponse of(User user, boolean isOnline) {
        return new UserDetailResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                isOnline
        );
    }
}
