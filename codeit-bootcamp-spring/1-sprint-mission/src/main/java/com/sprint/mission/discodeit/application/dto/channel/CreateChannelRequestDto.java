package com.sprint.mission.discodeit.application.dto.channel;

import jakarta.validation.constraints.NotBlank;

public record CreateChannelRequestDto(
        @NotBlank String name,
        @NotBlank String channelType
) {
}
