package com.sprint.mission.discodeit.entity.user.find;

import java.time.Instant;
import java.util.UUID;

public record UserFindResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String profileId,
    Boolean online
) {

}
