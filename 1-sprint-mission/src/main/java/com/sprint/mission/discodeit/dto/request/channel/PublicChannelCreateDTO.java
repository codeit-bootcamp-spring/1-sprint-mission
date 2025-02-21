package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;


public record PublicChannelCreateDTO(
    UUID ownerId,
    @NotBlank(message = "Username is required")
    String name,

    @NotBlank(message = "Description is required")
    String description
) {

}
