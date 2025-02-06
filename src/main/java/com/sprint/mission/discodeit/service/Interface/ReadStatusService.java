package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest request);
    Optional<ReadStatus> find (UUID id);
    List<ReadStatus> findAllByUserid(UUID userId);
    ReadStatus update(UpdateReadStatusRequest request);
    void delete(UUID id);
}
