package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void createChannel(UUID id, Channel channel);
    void updateChannelName(UUID id, String name);
    void updateDescript(UUID id, String descript);
    void deleteChannel(UUID id);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
}
