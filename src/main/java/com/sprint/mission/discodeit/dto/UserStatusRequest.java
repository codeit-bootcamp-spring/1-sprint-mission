package com.sprint.mission.discodeit.dto;

import java.time.Instant;

public record UserStatusRequest(
) {

  public record Update(
      Instant newLastActiveAt
  ) {

  }

}
