package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

public class UserStatusDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateUserStatusDTO {
        private UUID userId;
        private Instant lastLoginAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserStatusDTO {
        private Instant newLastLoginAt;
    }
}
