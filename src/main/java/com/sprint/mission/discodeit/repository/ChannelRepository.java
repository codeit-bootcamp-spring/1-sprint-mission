package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {

    Channel save(Channel channel);
    Channel findById(UUID id);
    Map<UUID, Channel> load();
    void delete(UUID id);
    boolean isChannelExsit(UUID uuid);

}
