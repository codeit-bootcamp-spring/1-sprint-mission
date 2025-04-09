package com.sprint.mission.discodeit.dto.message;

import static com.sprint.mission.discodeit.constant.ValidationConstants.MAXIMUM_MESSAGE_LENGTH;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateDto(
    @NotBlank(message = "content cannot be blank")
    @Size(max = MAXIMUM_MESSAGE_LENGTH, message = "content must be under " + MAXIMUM_MESSAGE_LENGTH
        + " characters")
    String newContent
) {

}
