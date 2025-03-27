package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

public record BinaryContentDto (
        UUID id,
        String fileName,
        String contentType,
        Long size,
        String filePath
) {
}
