package com.sprint.mission.discodeit.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public class ReadStatusDTO {

    @Builder
    public record request(UUID userId, UUID channelId, Instant lastReadAt) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID userId, UUID channelId, Instant lastReadAt) {
    }

    @Builder
    public record update(Long id, Instant lastReadAt) {
    }

    @Builder
    public record idResponse(Long id, UUID uuid) {
    }

}
