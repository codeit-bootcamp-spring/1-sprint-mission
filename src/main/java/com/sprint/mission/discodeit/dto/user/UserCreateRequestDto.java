package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequestDto(
        String name,
        String email,
        String password,
        byte[] binaryContentData
) {
}
