package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
    UUID id,
    String content,
    UUID channelId,
    UUID authorId,  // senderId에서 authorId로 변경
    Instant createdAt,
    Instant updatedAt
) {

  public static MessageDto from(Message message) {
    return new MessageDto(
        message.getId(),
        message.getContent(),
        message.getChannelId(),
        message.getAuthorId(),  // getSenderId()에서 getAuthorId()로 변경
        message.getCreatedAt(),
        message.getUpdatedAt()
    );
  }
}