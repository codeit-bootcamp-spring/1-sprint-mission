package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    public Map<UUID, Channel> loadChannelText();
    public void saveChannelText();
}
