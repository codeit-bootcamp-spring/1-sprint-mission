package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusDto {

  private UUID id;
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
}
