package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CreateReadStatusDto(
    @NotBlank
    String channelId,
    @NotBlank
    String userId,
    @NotNull
    Instant lastReadAt
) {

}
