package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    String content,
    UUID authorId,
    UUID channelId,
    Instant createdAt
) {

  public static MessageResponse from(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getContent(),
        message.getAuthorId(),
        message.getChannelId(),
        message.getCreatedAt()
    );
  }
}