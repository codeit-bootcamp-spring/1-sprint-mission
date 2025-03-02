package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    String content,
    UUID authorId,  // senderId에서 authorId로 변경
    UUID channelId,
    Instant createdAt
) {

  public static MessageResponse from(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getContent(),
        message.getAuthorId(),  // getSenderId()에서 getAuthorId()로 변경
        message.getChannelId(),
        message.getCreatedAt()
    );
  }
}