package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotBlank String fileName,
    @NotBlank String contentType,
    @NotNull byte[] bytes
) {

}
