package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentRequest(
        String fileName,
        String contentType,
        byte[] data
) {
}
