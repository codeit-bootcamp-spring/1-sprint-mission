package com.sprint.mission.discodeit.dto.binarycontent;

import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotNull
    String fileName,

    @NotNull
    String contentType,

    @NotNull
    byte[] bytes
) {

}
