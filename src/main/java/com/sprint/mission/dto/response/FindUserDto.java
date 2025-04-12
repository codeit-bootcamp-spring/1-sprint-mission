package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

import java.time.Instant;


@Schema(description = "유저 정보")
public record FindUserDto(
    UUID userId,
    Instant createAt,
    Instant updateAt,
    String name,
    String email,
    UUID profileImgId,
    boolean isOnline) {
}
