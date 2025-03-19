package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class ReadStatusDto {

  UUID id;
  UUID userId;
  UUID channelId;
  Instant lastReadAt;

}
