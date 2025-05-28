package com.sprint.mission.discodeit.entity.role;

import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,

    Role newRole
) {

}
