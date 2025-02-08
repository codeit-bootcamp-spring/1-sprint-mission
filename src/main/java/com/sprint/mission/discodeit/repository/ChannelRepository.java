package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    UUID save(Channel channel);
    Channel findOne(UUID id);
    List<Channel> findAll();
    UUID update(Channel channel);
    UUID delete (UUID id);
}
