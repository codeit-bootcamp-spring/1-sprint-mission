package com.sprint.mission.discodeit.application.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateMessageContentRequestDto(
        @NotNull UUID messageId,
        @NotBlank String content
) {
}
