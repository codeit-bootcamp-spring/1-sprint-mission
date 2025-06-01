package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
