package com.sprint.mission.discodeit.dto;

import java.util.UUID;
import javax.management.relation.Role;
import lombok.Getter;

@Getter
public class RoleUpdateRequest {

  private UUID userId;
  private Role newRole;
}
