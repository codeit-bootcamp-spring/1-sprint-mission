package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(ReadStatusCreateRequest readStatusRequest);

    ReadStatus findReadStatusById(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);


    ReadStatus updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest);

    void deleteReadStatusById(UUID readStatusId);

}
