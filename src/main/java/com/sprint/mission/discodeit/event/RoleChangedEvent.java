package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification.Type;
import com.sprint.mission.discodeit.entity.User.Role;
import java.util.UUID;

public record RoleChangedEvent(
    Type type,
    UUID userId,
    Role oldRole,
    Role newRole
) {
  public static RoleChangedEvent of(UUID userId, Role oldRole, Role newRole) {
    return new RoleChangedEvent(Type.ROLE_CHANGED, userId, oldRole, newRole);
  }
}
