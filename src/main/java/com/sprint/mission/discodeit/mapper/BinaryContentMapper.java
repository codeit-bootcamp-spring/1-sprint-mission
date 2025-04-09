package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    return BinaryContentDto.builder()
        .id(binaryContent.getId())  // UUID
        .fileName(binaryContent.getFileName())  // String
        .size(binaryContent.getSize())  // Long
        .contentType(binaryContent.getContentType())  // String
        .build();
  }

}
