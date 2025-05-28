package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User.Role;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    BinaryContentResponse profile,
    boolean online,
    Role role
) {

}
