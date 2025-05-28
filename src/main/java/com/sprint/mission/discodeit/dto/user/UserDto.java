package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.Role;
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
