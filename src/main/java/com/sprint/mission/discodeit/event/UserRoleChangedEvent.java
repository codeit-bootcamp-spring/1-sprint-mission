package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRoleChangedEvent {

    private final UUID userId;
    private final Role newRole;
    private final Role oldRole;
    private final String username;
}
