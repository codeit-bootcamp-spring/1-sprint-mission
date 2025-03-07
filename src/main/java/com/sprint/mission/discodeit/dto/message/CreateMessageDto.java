package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record CreateMessageDto(
    String content,
    String channelId,
    String authorId
) {

  public UUID getAuthorId() {
    return UUID.fromString(authorId);
  }

  public UUID getChannelId() {
    return UUID.fromString(channelId);
  }
}
