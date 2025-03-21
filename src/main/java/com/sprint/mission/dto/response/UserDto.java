package com.sprint.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "유저 정보")
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online) {
}
