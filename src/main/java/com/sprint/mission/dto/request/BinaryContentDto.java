package com.sprint.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public record BinaryContentDto(

        @Schema(example = "zessy")
        String fileName,
        String contentType,
        byte[] bytes) {
}
