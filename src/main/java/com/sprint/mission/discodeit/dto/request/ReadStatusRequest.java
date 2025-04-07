package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

public class ReadStatusRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private UUID userId;

    @NotNull(message = "채널 ID는 필수입니다.")
    private UUID channelId;

    @NotNull(message = "읽은 시간은 필수입니다.")
    private Instant lastReadAt;
  }

  @Getter
  @NoArgsConstructor
  public static class Update {

    @NotNull(message = "읽은 시간은 필수입니다.")
    private Instant newLastReadAt;
  }
}