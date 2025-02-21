package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelRepository {
    Channel createChannel(Channel channelInfoToCreate);
    Channel findChannelById(UUID key);
    Channel updateChannelById(UUID key, Channel channelInfoToUpdate);
    Channel deleteChannelById(UUID key);
}
