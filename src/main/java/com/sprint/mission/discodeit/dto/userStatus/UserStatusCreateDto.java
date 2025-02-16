package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusCreateDto(
        UUID userId
) {
    public static UserStatusCreateDto from(UUID userId) {
        return new UserStatusCreateDto(userId);
    }
}
