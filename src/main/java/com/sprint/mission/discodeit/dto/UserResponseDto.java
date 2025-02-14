package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        byte[] binaryContentData,
        OnlineStatus onlineStatus
) {

    public static UserResponseDto from(User user, BinaryContent binaryContent, OnlineStatus onlineStatus) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), binaryContent.getData(), onlineStatus);
    }
}
