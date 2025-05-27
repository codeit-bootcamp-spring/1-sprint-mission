package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserRoleUpdateRequest(
    @NotNull(message = "사용자 ID는 필수입니다")
    UUID userId,
    @NotNull(message = "권한은 필수입니다")
    Role newRole
) {

}
