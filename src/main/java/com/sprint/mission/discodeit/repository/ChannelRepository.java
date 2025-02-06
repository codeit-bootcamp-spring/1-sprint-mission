package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Optional<Channel> save(Channel channel);
    Optional<Channel> findById(UUID channelId);
    List<Channel> findAll();
    void deleteByChannelId(UUID channelId);
}
