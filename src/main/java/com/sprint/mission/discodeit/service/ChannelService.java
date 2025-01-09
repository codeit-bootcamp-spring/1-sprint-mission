package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(UUID id, String name);
    void deleteChannel(UUID id);
}
