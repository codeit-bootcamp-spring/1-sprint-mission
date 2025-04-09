package com.sprint.mission.discodeit.dto.user_status;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;


public record UpdateUserStatusDto(
    @NotBlank(message = "last activity time cannot be null")
    Instant newLastActivityAt
) {

}
