package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.PastOrPresent;

public record ReadStatusUpdateRequest(
    @PastOrPresent(message = "마지막 읽은 시간은 현재 또는 과거 시간이어야 합니다")
    Instant newLastReadAt,

    Boolean newNotificationEnabled
) {

}
