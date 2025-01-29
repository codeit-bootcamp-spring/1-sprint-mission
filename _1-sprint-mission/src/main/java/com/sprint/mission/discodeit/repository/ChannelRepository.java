package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel create(Channel channel);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    void delete(UUID channelId);
}
