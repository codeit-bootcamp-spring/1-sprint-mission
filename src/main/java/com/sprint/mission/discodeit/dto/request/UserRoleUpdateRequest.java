package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Role;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateRequest {

    @NotNull(message = "유저 Id를 입력하세요.")
    private UUID userId;

    @NotNull(message = "변경할 권한을 입력하세요.")
    private Role newRole;

}
