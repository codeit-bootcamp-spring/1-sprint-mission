package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import java.time.Instant;

public record NewMessageEvent(
    Instant createdAt,
    MessageDto messageDto
) {

  public NewMessageEvent(MessageDto messageDto) {
    this(Instant.now(), messageDto);
  }
} 