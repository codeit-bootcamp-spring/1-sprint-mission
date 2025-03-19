package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusDto {

  private UUID id;
  private UUID userId;
  private Instant lastActiveAt;
}
