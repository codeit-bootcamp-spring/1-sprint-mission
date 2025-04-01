package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "유저 아이디는 필수입니다.")
    UUID userId,

    @NotNull(message = "최근 활동기간은 필수입니다.")
    @PastOrPresent(message = "최근 활동 시간은 과거 또는 현재 시각이어야 합니다.")
    Instant lastActiveAt
) {

}
