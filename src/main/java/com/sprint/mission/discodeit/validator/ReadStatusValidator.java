package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.execption.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusValidator {

  private final ReadStatusRepository readStatusRepository;

  public ReadStatus validateReadStatusExistsById(UUID readStatusId) {
    return readStatusRepository.findReadStatusById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId)));
  }
}
