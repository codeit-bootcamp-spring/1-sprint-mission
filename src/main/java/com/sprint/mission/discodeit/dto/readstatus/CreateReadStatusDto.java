package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record CreateReadStatusDto(
    @NotBlank
    String channelId,
    @NotBlank
    String userId,
    Instant lastReadAt
) {
}
