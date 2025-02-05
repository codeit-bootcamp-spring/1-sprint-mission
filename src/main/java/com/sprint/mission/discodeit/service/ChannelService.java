package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel.ChannelType channelType, String title, String description);
    List<Channel> getAllChannelList();
    Channel searchById(UUID id);
    void updateChannel(UUID id, String title, String description);
    void deleteChannel(UUID id);
}
