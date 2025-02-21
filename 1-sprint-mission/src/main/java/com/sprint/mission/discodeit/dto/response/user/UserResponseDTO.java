package com.sprint.mission.discodeit.dto.response.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(UUID userId,
                              String username,
                              String email,
                              Instant createdAt,
                              Instant updatedAt,
                              boolean isOnline,
                              UUID profileId
) {

}
