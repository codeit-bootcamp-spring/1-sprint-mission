package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String phoneNumber,
        boolean isOnline
) {
    public static UserResponse fromEntity(User user, Boolean isOnline) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), isOnline);
    }
}
