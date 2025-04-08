package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
