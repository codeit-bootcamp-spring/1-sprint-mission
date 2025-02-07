package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);

    Optional<Channel> findByName(String channelName);

    Optional<Channel> findById(UUID channelId);

    List<Channel> findAll();

    Channel delete(Channel channel);
}
