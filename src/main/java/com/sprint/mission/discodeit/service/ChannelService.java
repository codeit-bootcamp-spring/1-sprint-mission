package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel);
    void update(UUID id,Channel channel);
    void delete(UUID id);
    List<Channel> readAll();
}
