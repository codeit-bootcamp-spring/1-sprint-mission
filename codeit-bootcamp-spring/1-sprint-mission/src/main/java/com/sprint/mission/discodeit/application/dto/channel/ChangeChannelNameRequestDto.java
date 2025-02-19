package com.sprint.mission.discodeit.application.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ChangeChannelNameRequestDto(
        @NotNull UUID channelId,
        @NotBlank String channelName
) {
}
