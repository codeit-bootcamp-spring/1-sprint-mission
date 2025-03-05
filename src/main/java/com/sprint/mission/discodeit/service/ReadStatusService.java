package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto createReadStatus(CreateReadStatusRequest createReadStatusRequest);

  ReadStatus findReadStatusById(UUID readStatusId);

  List<ReadStatusDto> findAllReadStatusesByUserId(UUID userId);

  ReadStatusDto updateReadStatus(UUID readStatusId,
      UpdateReadStatusRequest updateReadStatusRequest);

  void deleteReadStatus(UUID readStatusId);
}
