package com.spirnt.mission.discodeit.dto.binaryContent;

import com.spirnt.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BinaryContentDto {

    private UUID id;
    private String fileName;
    private Long size;
    private String contentType;

    @Builder
    public BinaryContentDto(UUID id, String fileName, Long size, String contentType) {
        this.id = id;
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
    }

    public static BinaryContentDto from(BinaryContent binaryContent) {
        if (binaryContent == null) {
            return null;
        }
        return BinaryContentDto.builder()
            .id(binaryContent.getId())
            .fileName(binaryContent.getFileName())
            .size(binaryContent.getSize())
            .contentType(binaryContent.getContentType())
            .build();
    }
}
