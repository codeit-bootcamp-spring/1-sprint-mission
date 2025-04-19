package com.sprint.mission.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull(message = "유저 ID는 필수입니다.")
        UUID userId,
        @NotNull(message = "채널 ID는 필수입니다.")
        UUID channelId,
        @NotNull(message = "마지막 읽은 시간은 필수입니다.")
        Instant lastReadAt) {
}
