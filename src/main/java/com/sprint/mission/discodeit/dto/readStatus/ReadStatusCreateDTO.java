package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusCreateDTO {


  @NotNull(message = "사용자 ID는 필수입니다.")
  private UUID userId;

  @NotNull(message = "채널 ID는 필수입니다.")
  private UUID channelId;

  @NotNull(message = "마지막 읽은 시각은 필수입니다.")
  @PastOrPresent(message = "마지막 읽은 시각은 과거 또는 현재여야 합니다.")
  private Instant lastReadAt;
}
