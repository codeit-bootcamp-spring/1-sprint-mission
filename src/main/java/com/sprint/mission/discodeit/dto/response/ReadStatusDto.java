package com.sprint.mission.discodeit.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusDto {

  UUID id;
  UUID userId;
  UUID channelId;
  boolean notificationEnabled;

  @NotNull
  Instant lastReadAt;
}