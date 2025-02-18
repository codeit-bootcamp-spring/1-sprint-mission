package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusService {

    ReadStatus createReadStatus(CreateReadStatusRequest createReadStatusRequest);

    ReadStatus findReadStatusById(UUID readStatusId);

    ReadStatus findAllReadStatusesByUserId(UUID userId);

    ReadStatus updateReadStatus(UUID readStatusId);

    void deleteReadStatus(UUID readStatusId);
}
