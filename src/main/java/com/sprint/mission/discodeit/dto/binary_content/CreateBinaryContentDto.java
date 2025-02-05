package com.sprint.mission.discodeit.dto.binary_content;

import com.sprint.mission.discodeit.util.FileType;
import jakarta.validation.constraints.NotBlank;

public record CreateBinaryContentDto(
    @NotBlank
    String userId,
    String messageId,
    @NotBlank
    String fileName,
    @NotBlank
    FileType fileType,
    long fileSize,
    byte[] data
) {
}
