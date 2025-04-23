package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateMessageRequest(
    String content,

    @NotBlank(message = "채널 ID를 작성해주세요.")
    UUID channelId,

    UUID authorId
) {

}
