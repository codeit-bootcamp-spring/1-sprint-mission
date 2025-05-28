package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequest(

    @NotNull
    UUID userId,

    @NotNull
    Role newRole
) {

}
