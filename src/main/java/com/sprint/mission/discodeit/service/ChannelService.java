package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(UUID id, Long createdAt, Long updatedAt, String name);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(UUID id, String name, Long updatedAt);
    void deleteChannel(UUID id);
}
