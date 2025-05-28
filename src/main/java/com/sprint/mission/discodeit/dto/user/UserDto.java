package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.security.Role;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    Role role,
    BinaryContentDto profile,
    Boolean online
) {
}
