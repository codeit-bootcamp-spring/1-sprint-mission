package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserFindResponse(
    UUID id,
    String username,
    String email,
    Instant createdAt,
    Instant updatedAt,
    UUID profileId,
    boolean online
) {

}
