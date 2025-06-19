package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import java.time.Instant;
import java.util.UUID;

public record RoleChangedEvent(
    Instant createdAt,
    UUID userId,
    Role previousRole,
    Role newRole
) {

  public RoleChangedEvent(UUID userId, Role previousRole, Role newRole) {
    this(Instant.now(), userId, previousRole, newRole);
  }
} 