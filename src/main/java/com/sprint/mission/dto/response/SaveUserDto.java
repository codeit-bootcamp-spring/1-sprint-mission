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

    public static SaveUserDto fromEntity(User user) {
        return new SaveUserDto(
                user.getId(),
                user.getCreateAt(),
                user.getUpdateAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileImgId()
        );
    }
}
