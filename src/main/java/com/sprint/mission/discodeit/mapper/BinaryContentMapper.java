package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }

    return new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType()
    );
  }

  public List<BinaryContentDto> toDtoList(List<BinaryContent> binaryContents) {
    if (binaryContents == null) {
      return Collections.emptyList();
    }

    return binaryContents.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}
