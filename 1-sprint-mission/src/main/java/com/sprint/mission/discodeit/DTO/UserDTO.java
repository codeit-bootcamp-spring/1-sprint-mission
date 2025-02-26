package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDTO(
        UUID id,
        String name,
        String email,
        Instant createdAt,
        Instant updatedAt,
        boolean userIsRead
) {
    public static UserDTO fromEntity(User user) {
        boolean userIsRead = false;
        if (user.getUserStatus() != null) {
            userIsRead = user.getUserStatus().isUserOnline();
        }
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                userIsRead
        );
    }

    public static List<UserDTO> fromEntity(List<User> users){
        return users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
