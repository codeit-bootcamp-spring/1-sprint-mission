package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Channel find(UUID channelId);
    List<Channel> findAll();
    void update(Channel channel, String name, String introduction);
    void delete(Channel channel);
}
