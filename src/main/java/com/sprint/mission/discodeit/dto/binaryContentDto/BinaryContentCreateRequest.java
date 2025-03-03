package com.sprint.mission.discodeit.dto.binaryContentDto;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
