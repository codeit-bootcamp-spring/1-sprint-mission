package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateMessageDto(
    @NotBlank
    String content,
    @NotBlank
    String channelId,
    @NotBlank
    String authorId
) {

  public UUID getAuthorId() {
    return UUID.fromString(authorId);
  }

  public UUID getChannelId() {
    return UUID.fromString(channelId);
  }
}
