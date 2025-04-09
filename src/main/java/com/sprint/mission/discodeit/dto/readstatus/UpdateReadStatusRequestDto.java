package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UpdateReadStatusRequestDto {

  @NotNull
  private Instant newLastReadAt;
}
