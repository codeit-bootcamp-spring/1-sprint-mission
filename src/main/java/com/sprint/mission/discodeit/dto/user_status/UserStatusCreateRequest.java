package com.sprint.mission.discodeit.dto.user_status;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    @NotNull(message = "시간은 필수 입력 값입니다.")
    Instant lastActiveAt
) {

}
