package com.sprint.mission.discodeit.dto.request.binary;

import java.util.UUID;

public record BinaryContentCreateDTO(
    UUID userId,
    UUID messageId,
    String filename,
    String contentType,
    byte[] bytes
) {

}
