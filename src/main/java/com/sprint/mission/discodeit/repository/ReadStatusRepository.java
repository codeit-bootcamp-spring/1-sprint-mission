package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusRepository {
    ReadStatus findByUserIdAndChannelId(String userId, String channelId);
    void save(ReadStatus readStatus);
}
