package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public interface ChannelRepository {

    void save(Channel channel);

    Channel findById(UUID uuid);

    HashMap<UUID, Channel> findAll();

    void delete(UUID uuid);
}
