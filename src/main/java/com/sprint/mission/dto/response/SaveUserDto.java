package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.User;

import java.time.Instant;
import java.util.UUID;

public record SaveUserDto(
        UUID userId,
        Instant createAt,
        Instant updateAt,
        String name,
        String email,
        UUID profileImgId){
}
