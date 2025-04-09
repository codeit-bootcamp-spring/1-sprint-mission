package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "New name cannot be blank")
    @Size(min = 1, max = 100, message = "New name must be between 1 and 100 characters")
    String newName,

    @NotBlank(message = "New description cannot be blank")
    @Size(min = 10, max = 500, message = "New description must be between 10 and 500 characters")
    String newDescription
) {

}
