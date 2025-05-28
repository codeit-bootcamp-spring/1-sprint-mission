package com.sprint.mission.discodeit.security.role;

import java.util.UUID;

public record RoleUpdateRequest(UUID userId, Role newRole) {

}
