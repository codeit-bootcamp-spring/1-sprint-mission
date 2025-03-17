package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus readStatus) {
    if (readStatus == null) {
      return null;
    }

    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }

  public List<ReadStatusDto> toDtoList(List<ReadStatus> readStatuses) {
    if (readStatuses == null) {
      return Collections.emptyList();
    }

    return readStatuses.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}