package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
