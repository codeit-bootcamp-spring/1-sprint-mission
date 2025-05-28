package com.spirnt.mission.discodeit.dto.auth;

import com.spirnt.mission.discodeit.entity.Role;
import java.util.UUID;

public record UserRoleUpdateRequest(
    UUID userId,
    Role newRole
) {

}
