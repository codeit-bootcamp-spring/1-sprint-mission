package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentCreateDTO(
        UUID contentId,
        String filePath
) {
}
