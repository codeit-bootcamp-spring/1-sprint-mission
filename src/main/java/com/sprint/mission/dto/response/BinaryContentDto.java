package com.sprint.mission.dto.response;

import com.sprint.mission.entity.addOn.BinaryContent;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    long size,
    String contentType,
    byte[] bytes) {
    public BinaryContentDto(BinaryContent binaryContent) {
        this(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType(),
                null // bytes는 null로 처리
        );
    }
}
