package com.sprint.mission.discodeit.dto.binarycontent;


public record BinaryContentRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}
