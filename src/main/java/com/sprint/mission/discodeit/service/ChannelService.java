package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.HashMap;
import java.util.UUID;

public interface ChannelService {

    void addChannel(Channel channel);


    Channel getChannel(UUID id);


    public HashMap<UUID, Channel> getAllChannels();


    void updateChannel(UUID id, String name );


    void deleteChannel(UUID id);
}