package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name, String description, ChannelType channelType);

    Channel findById(UUID channelId);

    List<Channel> findAll();

    Channel update(UUID channelId, String name, String description, ChannelType channelType);

    void delete(UUID channelId);
}
