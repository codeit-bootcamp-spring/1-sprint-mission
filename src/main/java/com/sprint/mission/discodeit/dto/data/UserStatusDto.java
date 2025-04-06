package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    @NotNull(message = "ID는 필수입니다.")
    UUID id,

    @NotNull(message = "사용자 ID는 필수입니다.")
    UUID userId,

    @NotNull(message = "마지막 활동 시간은 필수입니다.")
    Instant lastActiveAt) {

}