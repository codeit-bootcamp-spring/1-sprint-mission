package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusCreateDTO {

  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
}
