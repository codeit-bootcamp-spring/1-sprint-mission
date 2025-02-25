package com.sprint.mission.discodeit.dto.request.binarycontent;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
