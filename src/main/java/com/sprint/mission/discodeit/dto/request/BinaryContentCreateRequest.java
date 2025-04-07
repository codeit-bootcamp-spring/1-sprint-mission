package com.sprint.mission.discodeit.dto.request;

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