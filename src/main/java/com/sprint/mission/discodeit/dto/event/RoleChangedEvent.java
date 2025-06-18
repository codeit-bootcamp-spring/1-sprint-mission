package com.sprint.mission.discodeit.dto.event;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record RoleChangedEvent(
    UUID userId,
    Role oldRole,
    Role newRole
) {

}
