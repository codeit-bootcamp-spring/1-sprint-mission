package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequest(
        String name,
        String email,
        String password,
        byte[] binaryContentData
) {
}
