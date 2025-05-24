package com.sprint.mission.discodeit.dto.data;

import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    String role,
    Boolean online
) {

}
