package com.sprint.mission.discodeit.event;

import java.time.Instant;

import com.sprint.mission.discodeit.dto.data.MessageDto;

public record NewMessageEvent(
    Instant createdAt,
    MessageDto messageDto
) {
  public NewMessageEvent(MessageDto messageDto) {
    this(Instant.now(), messageDto);
  }
} 