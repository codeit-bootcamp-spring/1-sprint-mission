package com.sprint.mission.discodeit.dto.user.userStatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusDto {

  private UUID id;
  private UUID userId;
  private Instant lastActiveAt;
}
