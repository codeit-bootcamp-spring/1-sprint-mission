package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "업데이트할 읽은 시간은 필수입니다.")
    @PastOrPresent(message = "읽은 시간은 현재 시각 이전이어야 합니다.")
    Instant newLastReadAt
) {

}
