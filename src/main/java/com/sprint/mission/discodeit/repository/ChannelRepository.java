package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel saveChannel(Channel channel);

    Channel findChannelById(UUID channelId);

    List<Channel> findAllChannels();

    List<Channel> findAccessibleChannels(User user);

    void removeChannel(UUID channelId);
}
