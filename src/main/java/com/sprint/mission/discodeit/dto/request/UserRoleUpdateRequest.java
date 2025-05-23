package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.security.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequest(
    @NotNull
    UUID userId,
    @NotNull
    Role newRole
) {
}
