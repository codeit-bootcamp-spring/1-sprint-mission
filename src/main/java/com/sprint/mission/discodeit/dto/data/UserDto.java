package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.eenum.Role;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online,
    Role role
) {

}
