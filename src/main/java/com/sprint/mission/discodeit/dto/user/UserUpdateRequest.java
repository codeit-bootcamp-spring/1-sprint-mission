package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequest(
        String name,
        String email,
        String password,
        byte[] binaryContentData
) {
}
