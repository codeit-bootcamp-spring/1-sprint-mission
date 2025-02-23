package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserDetailDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        boolean isOnline
) {
    public static UserDetailDto of(UUID id, Instant createdAt, Instant updatedAt, String name, String email, boolean isOnline) {
        return new UserDetailDto(id, createdAt, updatedAt, name, email, isOnline);
    }
}
