package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel save(Channel channel);
    Optional<Channel> read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, Channel channel);
    boolean delete(UUID id);
}
