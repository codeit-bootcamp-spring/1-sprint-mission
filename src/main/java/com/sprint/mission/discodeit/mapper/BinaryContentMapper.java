package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toBinaryContentDto(BinaryContent binaryContent) {
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getCreatedAt(),
        binaryContent.getFileName(), (long) binaryContent.getContent().length,
        binaryContent.getContentType(), binaryContent.getContent());
  }

}
