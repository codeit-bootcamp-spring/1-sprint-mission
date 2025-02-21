package com.sprint.mission.discodeit.dto;

public record BinaryContentRequest(
        String fileName,
        String contentType,
        byte[] data
) {
}
