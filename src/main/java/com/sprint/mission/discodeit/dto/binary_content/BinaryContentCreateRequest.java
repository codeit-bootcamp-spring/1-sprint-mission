package com.sprint.mission.discodeit.dto.binary_content;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] file
) {

}

