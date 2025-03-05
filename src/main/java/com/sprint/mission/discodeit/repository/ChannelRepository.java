package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {
    Channel save(Channel channel);
    void deleteById(String id);
    Optional<Channel> findById(String id);
    List<Channel> findAll();
}