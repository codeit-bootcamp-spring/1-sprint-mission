package com.sprint.mission.discodeit.dto.userService;

public record UserProfileImageRequest(
        byte[] data,
        String contentType,
        String fileName
) {
}
