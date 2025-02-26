package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateReadStatusRequestDto {

  private UUID userId;
  private UUID channelId;
  Instant lastReadAt;
}
