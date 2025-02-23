package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentResponse(
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentResponse from(BinaryContent content) {
        return new BinaryContentResponse(
                content.getFileName(),
                content.getSize(),
                content.getContentType(),
                content.getBytes()
        );
    }
}
