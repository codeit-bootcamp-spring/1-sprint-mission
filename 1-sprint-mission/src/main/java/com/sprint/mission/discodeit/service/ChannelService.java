package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name);
    Channel findById(UUID id);
    List<Channel> findAll();
    Channel update(UUID id, String newName);
    void delete(UUID id);
}
