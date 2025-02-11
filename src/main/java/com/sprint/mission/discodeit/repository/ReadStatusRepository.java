package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);
}
