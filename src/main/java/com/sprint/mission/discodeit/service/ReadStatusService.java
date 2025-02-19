package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest request);

    Optional<ReadStatus> find(UUID id);

    List<ReadStatus> findAllByUserId(UUID userID);

    ReadStatus update(UpdateReadStatusRequest request);

    void delete(UUID id);
}
