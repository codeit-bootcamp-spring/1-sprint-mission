package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "마지막 활동 시간은 필수입니다")
    @PastOrPresent(message = "마지막 활동 시간은 현재 또는 과거 시간이어야 합니다")
    Instant newLastActiveAt
) {

}
