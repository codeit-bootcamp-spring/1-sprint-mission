package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

    Channel save(Channel channel);

    Channel findChannel(UUID channelId);

    List<Channel> findAll();

    void updateChannel(Channel channel);

    void deleteChannel(UUID channelId);
}
