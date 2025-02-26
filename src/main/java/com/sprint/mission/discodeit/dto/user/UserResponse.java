package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UUID binaryContentId,
        String imageUrl,
        OnlineStatus onlineStatus
) {
    public static UserResponse from(User user, UUID binaryContentId, String imageUrl, OnlineStatus onlineStatus) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), binaryContentId, imageUrl, onlineStatus);
    }
}
