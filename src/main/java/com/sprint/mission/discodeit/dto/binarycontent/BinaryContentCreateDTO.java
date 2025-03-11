package com.sprint.mission.discodeit.dto.binarycontent;


public record BinaryContentCreateDTO(
    String fileName,
    Long size,
    String contentType,
    Byte[] bytes
) {

}
