package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(ChannelType type, String channel, String description);


    Channel getChannel(UUID uuid);


    Map<UUID, Channel> getAllChannels();


    void updateChannel(UUID uuid, String newName, String description);


    void deleteChannel(UUID uuid);
}