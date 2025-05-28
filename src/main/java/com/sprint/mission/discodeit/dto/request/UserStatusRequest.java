package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class UserStatusRequest {

  @Getter
  @NoArgsConstructor
  public static class Update {

    @NotNull(message = "최근 활동 시간은 필수입니다.")
    private Instant newLastActiveAt;
  }
}
