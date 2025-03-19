package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotBlank(message = "File name is required")
    String fileName,
    @NotNull(message = "Bytes are required")
    byte[] bytes,
    @NotBlank(message = "Content type is required")
    String contentType
) {

}
