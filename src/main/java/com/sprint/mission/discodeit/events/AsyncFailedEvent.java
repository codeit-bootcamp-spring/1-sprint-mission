package com.sprint.mission.discodeit.events;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsyncFailedEvent implements Serializable {

  private UUID fileId;
  private UUID requestId;
  private String message;

}
