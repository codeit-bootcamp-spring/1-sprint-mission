package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

public class ReadStatusDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReadStatusDTO {
        private UUID userId;
        private UUID channelId;
        private Instant lastReadAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateReadStatusDTO {
        private Instant newLastReadAt;
    }
}
