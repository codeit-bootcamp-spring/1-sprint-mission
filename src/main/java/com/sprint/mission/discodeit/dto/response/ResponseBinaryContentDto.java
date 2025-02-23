package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record ResponseBinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        long size
) {
}
