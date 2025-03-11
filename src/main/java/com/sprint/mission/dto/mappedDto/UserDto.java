package com.sprint.mission.dto.mappedDto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online) {
}
