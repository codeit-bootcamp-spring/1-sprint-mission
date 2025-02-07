package com.sprint.mission.discodeit.service.readStatus;

import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.UUID;
import java.util.List;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDTO request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID user);
    ReadStatus update(ReadStatusUpdateRequestDTO request);
    void delete(UUID readStatusId);
}
