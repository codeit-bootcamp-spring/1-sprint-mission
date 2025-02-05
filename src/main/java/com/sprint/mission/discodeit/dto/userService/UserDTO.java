package com.sprint.mission.discodeit.dto.userService;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        boolean isOnline
    ) {
    public static UserDTO from(User user, UserStatus userStatus) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                userStatus != null && userStatus.isOnline()
        );
    }
}
