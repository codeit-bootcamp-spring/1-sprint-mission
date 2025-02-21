package com.sprint.mission.discodeit.dto.binary_content;

import com.sprint.mission.discodeit.util.FileType;

public record BinaryContentDto(
    String fileName,
    FileType fileType,
    long fileSize,
    byte[] data
) {
}
