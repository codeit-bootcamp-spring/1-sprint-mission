package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserLoginResponseDTO(UUID id, String username, String email, Instant createdAt, Instant updatedAt, boolean isOnline) {
}
