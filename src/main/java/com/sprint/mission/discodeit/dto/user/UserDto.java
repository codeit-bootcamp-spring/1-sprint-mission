package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean isOnline
) {

}
