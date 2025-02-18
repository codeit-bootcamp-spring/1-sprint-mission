package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserInfoDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String email,
        boolean isOnline
) {
    public static UserInfoDto of(UUID id, Instant createdAt, Instant updatedAt, String name, String email, boolean isOnline) {
        return new UserInfoDto(id, createdAt, updatedAt, name, email, isOnline);
    }
}
