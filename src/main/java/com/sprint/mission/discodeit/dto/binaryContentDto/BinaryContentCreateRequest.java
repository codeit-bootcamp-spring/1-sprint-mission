package com.sprint.mission.discodeit.dto.binaryContentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotBlank(message = "File name cannot be blank.")
    @Size(min = 1, max = 255, message = "File name must be between 1 and 255 characters.")
    String fileName,

    @NotBlank(message = "Content type cannot be blank.")
    @Size(min = 1, max = 100, message = "Content type must be between 1 and 100 characters.")
    String contentType,

    @NotBlank(message = "Bytes cannot be blank.")
    byte[] bytes
) {

}
