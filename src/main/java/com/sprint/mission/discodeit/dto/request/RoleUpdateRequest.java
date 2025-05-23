package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;
import javax.management.relation.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    private UUID userId;
    private Role newRole;

}
