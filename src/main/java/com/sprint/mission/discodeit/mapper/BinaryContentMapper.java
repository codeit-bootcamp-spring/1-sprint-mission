package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

  private final BinaryContentStorage binaryContentStorage;

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    byte[] file = null;
    try {
      file = binaryContentStorage.get(binaryContent.getId()).readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getFilename(),
        file,
        binaryContent.getContentType(),
        binaryContent.getCreatedAt(),
        binaryContent.getSize()
    );
  }
}
