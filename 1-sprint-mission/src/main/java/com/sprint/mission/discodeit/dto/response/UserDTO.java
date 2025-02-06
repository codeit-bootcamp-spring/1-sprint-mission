package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(UUID id, String username, String email, Instant createdAt, Instant updatedAt, boolean isOnline) {}
