package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Optional<Channel> findChannelById(UUID id);

    Optional<Channel> findChannelByName(String name);

    List<Channel> findAllChannels();

    void save(Channel channel);

    void remove(UUID id);
}
