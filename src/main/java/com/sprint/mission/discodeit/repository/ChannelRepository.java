package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void createChannel(Channel channel);
    Optional<Channel> getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(UUID id, String channelName);
    void deleteChannel(UUID id);
}
