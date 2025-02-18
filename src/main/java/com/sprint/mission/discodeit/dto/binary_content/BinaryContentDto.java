package com.sprint.mission.discodeit.dto.binary_content;

public record BinaryContentDto(
    String fileName,
    String fileType,
    long fileSize,
    byte[] data
) {
}
