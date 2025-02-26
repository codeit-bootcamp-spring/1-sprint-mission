package com.sprint.mission.discodeit.dto.user_status;

import java.time.Instant;

public record UserStatusResponseDto (
    String id,
    Instant createdAt,
    Instant updatedAt,
    String userId,
    Instant lastActivityAt,
    boolean online
){
}
