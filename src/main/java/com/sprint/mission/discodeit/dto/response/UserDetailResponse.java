package com.sprint.mission.discodeit.dto.response;

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
    public static UserDetailResponse of(UUID id, Instant createdAt, Instant updatedAt, String name, String email, boolean isOnline) {
        return new UserDetailResponse(id, createdAt, updatedAt, name, email, isOnline);
    }
}
