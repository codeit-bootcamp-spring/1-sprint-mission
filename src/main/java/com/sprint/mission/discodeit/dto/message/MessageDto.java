package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID messageId,
    UUID channelId,
    UUID writerId,
    String content,
    List<BinaryContent> attachments
) {

  public static MessageDto fromEntity(Message message) {
    return new MessageDto(
        message.getId(),
        message.getChannel().getId(),
        message.getWriter().getId(),
        message.getContent(),
        message.getAttachments()
    );
  }
}
