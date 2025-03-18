package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusCreateRequest {

  private UUID userId;
  private UUID channelId;
  Instant lastReadAt;
}
