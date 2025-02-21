package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponse create(ReadStatusRequest request);
    ReadStatusResponse findById(UUID id);
    ReadStatus findByIdOrThrow(UUID id);
    List<ReadStatusResponse> findAllByUserId(UUID userId);
    List<ReadStatusResponse> findAllByChannelId(UUID channelId);
    ReadStatusResponse update(UUID id);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID channelId);
}
