package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;

public record CreateReadStatusDto(
    @NotBlank
    String channelId,
    @NotBlank
    String userId
) {
}
