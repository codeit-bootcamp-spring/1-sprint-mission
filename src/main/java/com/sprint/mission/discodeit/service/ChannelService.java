package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Optional<Channel> readChannel(UUID id);
    List<Channel> readAllChannels();
    void updateChannel(UUID id, String name, String topic);
    void deleteChannel(UUID id);
}