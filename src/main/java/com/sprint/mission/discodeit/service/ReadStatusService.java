package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatusType;

public interface ReadStatusService {
    ReadStatusDTO create(ReadStatusDTO readStatusDTO);
    ReadStatusDTO find(String userId, String channelId);
    void delete(String userId, String channelId);
    ReadStatusType readOnOff(String userId);
}