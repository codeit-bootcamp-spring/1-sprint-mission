package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    void create(Channel channel);
    Optional<Channel> findById(String id);
    List<Channel> findAll();
    void update(String id, String newName);
    void delete(String id);
}
