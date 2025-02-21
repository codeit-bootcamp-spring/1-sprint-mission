package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentDto(
        String fileName,
        String contentType,
        byte[] data
) { }
