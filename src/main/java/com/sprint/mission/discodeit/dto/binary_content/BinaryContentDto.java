package com.sprint.mission.discodeit.dto.binary_content;


import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    String contentType,
    Long size
) {

}
