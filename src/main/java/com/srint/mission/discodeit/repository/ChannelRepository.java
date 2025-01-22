package com.srint.mission.discodeit.repository;

import com.srint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    UUID save(Channel channel);
    Channel findOne(UUID id);
    List<Channel> findAll();
    UUID delete (UUID id);
}
