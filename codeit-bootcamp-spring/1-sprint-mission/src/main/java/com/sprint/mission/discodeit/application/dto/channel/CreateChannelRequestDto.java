package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.domain.channel.enums.ChannelType;
import jakarta.validation.constraints.NotBlank;

public record CreateChannelRequestDto(
        @NotBlank String name,
        @NotBlank ChannelType channelType
) {
}
