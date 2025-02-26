package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
            UUID userId,
            Instant createdAt,
            Instant updatedAt,
            String userName,
            String email,
            UUID profileId,
            boolean isOnline
){}
