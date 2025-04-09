package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    String content,
    @NotNull(message = "Channel ID cannot be null")
    UUID channelId,
    @NotNull(message = "Author ID cannot be null")
    UUID authorId
) {

}
