package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateRequest(
    @NotBlank(message = "ReadStatus ID는 필수입니다.")
    UUID id,
    Instant channelLastReadTimes
) {

}
