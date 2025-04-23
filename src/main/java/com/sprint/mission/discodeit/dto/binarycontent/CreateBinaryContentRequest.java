package com.sprint.mission.discodeit.dto.binarycontent;

public record CreateBinaryContentRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
