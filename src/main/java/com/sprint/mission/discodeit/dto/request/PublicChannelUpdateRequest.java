package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
        @NotBlank(message = "Channel name cannot be blank.")
        @Size(min = 1, max = 100, message = "Channel name must be between 1 and 100 characters.")
        String newName,

        @NotBlank(message = "Channel description cannot be blank.")
        @Size(min = 1, max = 500, message = "Channel description must be between 1 and 500 characters.")
        String newDescription
) {

}
