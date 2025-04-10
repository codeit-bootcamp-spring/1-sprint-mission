package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponse create(ReadStatusRequest request);
    ReadStatusResponse markMessageAsRead(UUID messageId);
    List<ReadStatusResponse> getUserMessageReadStatus(UUID userId, UUID channelId);
}
