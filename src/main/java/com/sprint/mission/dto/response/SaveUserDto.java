package com.sprint.mission.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SaveUserDto(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    String name,
    String email,
    UUID profileImgId) {

}
