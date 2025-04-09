package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateReadStatusRequestDto {

  @NotNull
  private UUID userId;
  @NotNull
  private UUID channelId;
  @NotNull
  Instant lastReadAt;
}
