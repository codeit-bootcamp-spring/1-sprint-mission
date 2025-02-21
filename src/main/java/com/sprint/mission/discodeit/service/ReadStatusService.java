package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDTO.response create(ReadStatusDTO.request request);
    ReadStatusDTO.response markMessageAsRead(UUID messageId);
    List<ReadStatusDTO.response> getUserMessageReadStatus(UUID userId, UUID channelId);
}
