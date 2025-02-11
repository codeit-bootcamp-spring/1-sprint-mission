package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
