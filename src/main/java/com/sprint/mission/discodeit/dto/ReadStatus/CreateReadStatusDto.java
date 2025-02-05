package com.sprint.mission.discodeit.dto.ReadStatus;

import jakarta.validation.constraints.NotBlank;

public record CreateReadStatusDto(
    @NotBlank
    String channelId,
    @NotBlank
    String userId
) {
}
