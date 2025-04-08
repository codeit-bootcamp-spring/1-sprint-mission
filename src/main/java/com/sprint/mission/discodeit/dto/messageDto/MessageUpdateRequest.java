package com.sprint.mission.discodeit.dto.messageDto;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
    @NotBlank(message = "Message content cannot be blank.")
    String newContent
) {

}
