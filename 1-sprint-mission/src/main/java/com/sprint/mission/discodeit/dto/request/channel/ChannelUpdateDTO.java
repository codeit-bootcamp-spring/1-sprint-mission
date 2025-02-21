package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ChannelUpdateDTO(
    UUID channelId,

    @NotBlank(message = "Username is required")
    String newName,

    @NotBlank(message = "Description is required")
    String newDescription
) {

}
