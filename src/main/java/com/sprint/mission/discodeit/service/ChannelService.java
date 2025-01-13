package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(Channel channel);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String name, String type);
    boolean delete(UUID id);
}
