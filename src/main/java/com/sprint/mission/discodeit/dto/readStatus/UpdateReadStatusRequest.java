package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpdateReadStatusRequest(
    
    @NotNull(message = "마지막으로 메시지를 읽은 시간은 필수입니다.")
    Instant newLastReadAt
) {

}
