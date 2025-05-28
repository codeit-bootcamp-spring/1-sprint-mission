package com.sprint.mission.discodeit.dto.user;


import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;

import com.sprint.mission.discodeit.entity.UserRole;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online,
    UserRole role
) {


}
