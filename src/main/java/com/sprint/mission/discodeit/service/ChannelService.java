package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    void create(Channel channel);
    Optional<Channel> read(UUID id);
    List<Channel> readAll();
    void update(UUID id, Channel channel);
    void delete(UUID id);
}
