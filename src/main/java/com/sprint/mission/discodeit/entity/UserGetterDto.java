package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public record UserGetterDto(
        Instant createdAt,
        Instant updatedAt,
        UUID id,
        String email,
        String userName,
        String password,
        String profilePicture
) { public static UserGetterDto from(User user) {
    return new UserGetterDto(user.getCreatedAt(), user.getUpdatedAt(), user.getId(), user.getEmail(), user.getUserName(), user.getPassword(), user.getProfilePicture());
    }
}
