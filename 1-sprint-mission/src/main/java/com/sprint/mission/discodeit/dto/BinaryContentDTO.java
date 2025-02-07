package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        String filename,
        byte[] fileData
) {
    public static BinaryContentDTO fromEntity(BinaryContent binaryContent) {
        return new BinaryContentDTO(
                binaryContent.getId(),
                binaryContent.getFilename(),
                binaryContent.getFileData()
        );
    }
}
