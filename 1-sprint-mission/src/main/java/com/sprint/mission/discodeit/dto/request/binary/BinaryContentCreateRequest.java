package com.sprint.mission.discodeit.dto.request.binary;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes

) {

}
