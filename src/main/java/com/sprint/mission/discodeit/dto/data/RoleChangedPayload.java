package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangedPayload {
  private String userId;
  private String oldRole;
  private String newRole;
}
