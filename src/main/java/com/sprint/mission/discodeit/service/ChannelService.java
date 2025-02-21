package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    Channel registerChannel(Channel channelInfoToCreate);
    Channel searchChannelById(UUID key);
    Channel updateChannelById(UUID key, Channel channelInfoToUpdate);
    Channel deleteChannelById(UUID key);
}
