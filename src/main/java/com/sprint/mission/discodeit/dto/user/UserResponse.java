package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        Instant createAt,
        Instant updateAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static UserResponse from(User user, UUID profileId, Boolean online) {
        return new UserResponse(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
            user.getUsername(), user.getEmail(), profileId, online);
    }
}
