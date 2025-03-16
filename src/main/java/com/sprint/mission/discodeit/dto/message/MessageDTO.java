package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageDTO(
    UUID messageId,
    UUID channelId,
    UUID writerId,
    String content,
    List<UUID> attachmentIds
) {

  public static MessageDTO fromEntity(Message message) {
    return new MessageDTO(
        message.getId(),
        message.getChannelId(),
        message.getWriterId(),
        message.getContent(),
        message.getAttachmentIds()
    );
  }
}
