package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BinaryContentCreateRequest(
    @NotNull
    String fileName,
    @NotNull
    @Size(min = 1, max = 100)
    String contentType,
    @NotNull
    @NotEmpty
    byte[] bytes
) {

}
