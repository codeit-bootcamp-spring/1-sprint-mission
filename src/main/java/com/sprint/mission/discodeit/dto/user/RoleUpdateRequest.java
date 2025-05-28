package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.security.Role;
import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
