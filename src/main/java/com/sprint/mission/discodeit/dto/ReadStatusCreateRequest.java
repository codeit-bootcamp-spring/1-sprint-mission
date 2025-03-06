package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    @NotNull(message = "시간은 필수 입력 값입니다.")
    Instant lastReadAt
) {

}
