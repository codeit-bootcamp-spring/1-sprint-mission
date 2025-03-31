package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "User ID cannot be null") // 사용자 ID가 null이 아니어야 합니다.
    UUID userId,

    @NotNull(message = "Last active at cannot be null") // 마지막 활동 시간이 null이 아니어야 합니다.
    @PastOrPresent(message = "Last active at must be in the past or the present") // 마지막 활동 시간은 과거나 현재여야 합니다.
    Instant lastActiveAt
) {

}
