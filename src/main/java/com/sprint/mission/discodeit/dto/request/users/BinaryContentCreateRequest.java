package com.sprint.mission.discodeit.dto.request.users;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
