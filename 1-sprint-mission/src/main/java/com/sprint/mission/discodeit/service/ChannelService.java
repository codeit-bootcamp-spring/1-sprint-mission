package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;

public interface ChannelService {
    Channel create(Channel channel);
    Channel findById(String id);
    List<Channel> findAll();
    Channel update(String id, Channel updated);
    void delete(String id);
}
