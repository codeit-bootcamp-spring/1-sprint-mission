package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID messageId,
    UUID channelId,
    UUID writerId,
    String content,
    List<BinaryContent> attachments
) {

}
