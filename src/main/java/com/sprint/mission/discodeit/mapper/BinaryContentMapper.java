package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BinaryContentMapper {
    public BinaryContentResponseDto toResponseDto(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
                .id(binaryContent.getId())
                .fileName(binaryContent.getFilename())
                .size(binaryContent.getFileSize())
                .contentType(binaryContent.getContentType())
                .build();
    }

    public BinaryContent toEntity(MultipartFile file) {
        return new BinaryContent(file.getOriginalFilename(), file.getContentType(), file.getSize());
    }

    public BinaryContent toEntity(BinaryContentResponseDto binaryContentResponseDto) {
        return new BinaryContent(binaryContentResponseDto.fileName(), binaryContentResponseDto.contentType(), binaryContentResponseDto.size());
    }
}
