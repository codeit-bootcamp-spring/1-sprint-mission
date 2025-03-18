package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
