package com.sprint.mission.discodeit.entity.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageUpdateRequest (

        @NotNull
        UUID channelId,

        @NotBlank
        String newTitle,

        @NotBlank
        String newContent
) {
}
