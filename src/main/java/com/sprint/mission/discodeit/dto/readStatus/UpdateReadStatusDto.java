package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpdateReadStatusDto(
    @NotNull
    Instant newLastReadAt
) {

}
