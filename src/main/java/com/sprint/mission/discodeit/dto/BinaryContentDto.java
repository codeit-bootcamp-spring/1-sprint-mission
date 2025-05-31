package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    Long size,
    String fileName,
    String contentType
) {

}
