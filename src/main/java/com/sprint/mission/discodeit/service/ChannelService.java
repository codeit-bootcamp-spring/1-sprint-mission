package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String channel);


    Channel getChannel(UUID id);


    Map<UUID, Channel> getAllChannels();


    void updateChannel(UUID id, String name );


    void deleteChannel(UUID id);
}