package com.sprint.mission.discodeit.dto.response;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;


public record UserCreateResponse(
        UUID id, Instant createdAt, String username, String email, UUID profileId
) {

    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId()
        );
    }
}
