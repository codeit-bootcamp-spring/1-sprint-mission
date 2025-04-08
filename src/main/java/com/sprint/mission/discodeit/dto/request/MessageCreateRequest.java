package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(
        @NotBlank(message = "Message content cannot be blank.")
        String content,

        @NotNull(message = "Channel id cannot null.")
        UUID channelId,

        @NotNull(message = "Author id cannot null.")
        UUID authorId
) {

}
