package com.sprint.mission.discodeit.events;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleEvent implements Serializable {

  private UUID userId;
  private String currentRole;
  private String preRole;

}
