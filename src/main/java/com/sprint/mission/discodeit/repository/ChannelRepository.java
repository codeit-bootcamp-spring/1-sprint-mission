package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    UUID save(String channelName);
    Channel findById(UUID id);
    List<Channel> findAll();
    boolean delete(UUID channelId);
    void update(UUID id, String name);
}
