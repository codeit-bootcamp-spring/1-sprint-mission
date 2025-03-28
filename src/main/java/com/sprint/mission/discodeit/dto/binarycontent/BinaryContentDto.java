package com.sprint.mission.discodeit.dto.binarycontent;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
