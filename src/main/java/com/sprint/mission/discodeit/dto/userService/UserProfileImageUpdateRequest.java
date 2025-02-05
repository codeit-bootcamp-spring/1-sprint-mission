package com.sprint.mission.discodeit.dto.userService;

public record UserProfileImageUpdateRequest(
        byte[] data,
        String contentType,
        String fileName
) {
}
