package com.sprint.mission.discodeit.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public class UserStatusDTO {
    @Builder
    public record request(UUID userId, Instant lastAccessedAt) {
    }

    @Builder
    public record response(UUID userStatusId, UUID userId, Instant lastAccessedAt) {
    }

    @Builder
    public record update(Long id, UUID userId, Instant lastAccessedAt) {
    }
}
