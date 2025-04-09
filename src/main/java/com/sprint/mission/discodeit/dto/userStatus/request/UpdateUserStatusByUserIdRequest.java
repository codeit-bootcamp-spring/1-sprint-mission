package com.sprint.mission.discodeit.dto.userStatus.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpdateUserStatusByUserIdRequest(

    @NotNull(message = "lastActiveAt은 null일 수 없습니다.")
    Instant newLastActiveAt
) {

}
