package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  void create(ReadStatusDto createReadStatusDto);

  ReadStatus findById(UUID readStatusId);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  void updateReadStatus(UUID userId, UUID channelId);

  void remove(UUID readStatusId);
}
