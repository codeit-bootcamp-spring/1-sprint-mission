package com.sprint.mission.discodeit.dto.userStatusDto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(

    @NotNull(message = "마지막 접속 시간은 필수입니다.")
    Instant newLastActiveAt
) {

}
