package com.sprint.mission.discodeit.dto.event;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record UserRoleUpdateEvent(
  UUID receivedId,
  Role role
) {

}
