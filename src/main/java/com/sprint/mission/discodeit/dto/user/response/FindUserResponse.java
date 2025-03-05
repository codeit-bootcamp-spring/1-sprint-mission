package com.sprint.mission.discodeit.dto.user.response;


import java.time.Instant;
import java.util.UUID;

public record FindUserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

}
