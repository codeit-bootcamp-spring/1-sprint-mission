package com.sprint.mission.discodeit.auth;

import java.util.UUID;

public record RoleUpdateRequest(UUID userId, Role newRole) {

}
