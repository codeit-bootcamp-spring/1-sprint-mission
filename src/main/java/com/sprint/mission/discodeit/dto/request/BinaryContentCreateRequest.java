package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotBlank(message = "File name cannot be empty")
    String fileName,
    @NotBlank(message = "Content type cannot be empty")
    String contentType,
    @NotNull(message = "Byte array cannot be null") // 바이트 배열이 null이 아니어야 합니다.
    @Size(min = 1, message = "Byte array cannot be empty") // 바이트 배열이 비어 있지 않아야 합니다.
    byte[] bytes
) {

}
