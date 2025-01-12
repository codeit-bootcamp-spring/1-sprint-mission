package com.sprint.mission.discodeit.entity.channel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateNewChannelRequest(
        @NotNull
        UUID userId,
        @NotBlank
        String channelName
) {
}
