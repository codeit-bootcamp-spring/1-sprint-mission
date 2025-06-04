package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record RoleUpdateRequest(
    UUID userId,
    String newRole
) {


}
