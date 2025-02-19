package com.sprint.mission.discodeit.application.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateMessageRequestDto(
        @NotBlank String content,
        @NotNull UUID userId,
        @NotNull UUID destinationChannelId
) {
}
