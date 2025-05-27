package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RoleUpdateRequest(
    @NotNull(message = "User ID is required")
    UUID userId,

    @NotNull(message = "New role is required")
    Role newRole
) {

}