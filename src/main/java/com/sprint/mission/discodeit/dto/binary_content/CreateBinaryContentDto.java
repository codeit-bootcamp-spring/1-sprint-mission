package com.sprint.mission.discodeit.dto.binary_content;

import jakarta.validation.constraints.NotBlank;

public record CreateBinaryContentDto(
    @NotBlank
    String userId,
    String messageId,
    @NotBlank
    String fileName,
    @NotBlank
    String fileType,
    long fileSize,
    byte[] data,
    boolean isProfile
) {
    public CreateBinaryContentDto(String userId, String messageId, String fileName, String fileType, long fileSize, byte[] data) {
        this(userId, messageId, fileName, fileType, fileSize, data, false);
    }
}
