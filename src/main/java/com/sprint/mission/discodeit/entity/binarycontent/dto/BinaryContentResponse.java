package com.sprint.mission.discodeit.entity.binarycontent.dto;

import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
