package com.sprint.mission.discodeit.security;

import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    Role newRole
) {
}
