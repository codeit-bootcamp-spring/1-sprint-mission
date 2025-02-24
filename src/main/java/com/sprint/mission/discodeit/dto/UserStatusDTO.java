package com.sprint.mission.discodeit.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public class UserStatusDTO {

    @Builder
    public record request(UUID userId, Instant accessedAt) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID userId, Instant accessedAt) {
    }

    @Builder
    public record update(Long id, UUID userId, Instant accessedAt) {
    }

    @Builder
    public record idResponse(Long id, UUID uuid) {
    }

}
