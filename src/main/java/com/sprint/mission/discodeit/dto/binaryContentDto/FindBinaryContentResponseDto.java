package com.sprint.mission.discodeit.dto.binaryContentDto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public class FindBinaryContentResponseDto {
    UUID id;
    String fileName;
    String contentType;
    Long size;
    String filePath;

    public FindBinaryContentResponseDto(BinaryContent binaryContent) {
        this.id = binaryContent.getId();
        this.fileName = binaryContent.getFileName();
        this.contentType = binaryContent.getContentType();
        this.size = binaryContent.getSize();
        this.filePath = binaryContent.getFilePath();
    }
}
