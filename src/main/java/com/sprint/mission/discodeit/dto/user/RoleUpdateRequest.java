package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import lombok.Getter;

@Getter
public class RoleUpdateRequest {

  private UUID userId;
  private Role newRole;
}
