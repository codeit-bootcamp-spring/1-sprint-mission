package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpdateUserStatusDto(
    @NotNull
    Instant updateAt
) {

}
