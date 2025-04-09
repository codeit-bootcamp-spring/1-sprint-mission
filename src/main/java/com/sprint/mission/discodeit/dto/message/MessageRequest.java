package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record MessageRequest(
    @NotNull(message = "메시지 내용을 입력하세요")
    String content,
    UUID channelId,
    UUID userId,
    List<BinaryContent> attachments
) {

}
