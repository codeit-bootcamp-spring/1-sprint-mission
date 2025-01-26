package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    public void save(Map<UUID, Channel> channelList);
    public Map<UUID, Channel> load();
}
