package com.sprint.mission.discodeit.dto.message;

import static com.sprint.mission.discodeit.constant.ValidationConstants.MAXIMUM_MESSAGE_LENGTH;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMessageDto(

    @Size(max = MAXIMUM_MESSAGE_LENGTH, message = "content must be under 500 characters")
    String content,
    @NotBlank(message = "channel must be specified")
    String channelId,
    @NotBlank(message = "author must be specified")
    String authorId
) {

}
