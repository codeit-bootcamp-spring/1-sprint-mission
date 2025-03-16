package com.sprint.mission.discodeit.dto.read_status;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "시간은 필수 입력 값입니다.")
    Instant newLastReadAt
) {

}
