package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User.Role;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online,
    Role role
) {

}
