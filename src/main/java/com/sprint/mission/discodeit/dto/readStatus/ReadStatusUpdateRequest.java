package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
    @NotNull(message = "lastReadAt 필드는 반드시 값이 필요합니다.")
    Instant lastReadAt,
    boolean newNotificationEnabled
) {

}
