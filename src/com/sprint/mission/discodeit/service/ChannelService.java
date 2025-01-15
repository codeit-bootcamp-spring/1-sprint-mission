package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channelToCreate);

    Channel findChannelById(UUID key);

    Channel updateChannelById(UUID key, Channel channelToUpdate);

    Channel deleteChannelById(UUID key);
}
