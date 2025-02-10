package com.sprint.mission.discodeit.application.dto.message;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeleteMessageRequestDto(
        @NotNull UUID messageId
) {
}
