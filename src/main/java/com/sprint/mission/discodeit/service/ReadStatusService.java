package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponse create(UUID userId, ReadStatusRequest.Create request);

    ReadStatusResponse findById(UUID id);

    List<ReadStatusResponse> findAllByUserId(UUID userId);

    List<ReadStatusResponse> findAllByChannelId(UUID channelId);

    ReadStatusResponse update(UUID userId, UUID id, ReadStatusRequest.Update request);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}
