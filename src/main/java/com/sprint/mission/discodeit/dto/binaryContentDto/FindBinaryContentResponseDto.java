package com.sprint.mission.discodeit.dto.binaryContentDto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindBinaryContentResponseDto {
    UUID id;
    String fileName;
    String contentType;
    Long size;
    String filePath;

    public static FindBinaryContentResponseDto fromEntity(BinaryContent binaryContent) {
        return new FindBinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getSize(),
                binaryContent.getFilePath()
        );
    }
}
