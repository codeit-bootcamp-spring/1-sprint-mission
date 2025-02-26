package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentCreateDto(
        String fileName,
        String contentType,
        byte[] bytes
) { }
