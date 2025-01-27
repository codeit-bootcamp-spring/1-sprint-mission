package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {
    Channel save(Channel channel);

    Optional<Channel> findByName(String channelName);

    List<Channel> findAll();

    Channel delete(Channel channel);
}
