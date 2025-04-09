package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @NotNull(message = "업데이트할 활동 시간은 필수입니다.")
        Instant newLastActiveAt
) {
}
