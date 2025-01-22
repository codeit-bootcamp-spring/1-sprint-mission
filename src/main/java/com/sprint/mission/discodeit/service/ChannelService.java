package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Optional<Channel> findChannel(UUID id);
    List<Channel> findAllChannels();
    void updateChannelName(UUID id,String channelName);
    void updateChannelDescription(UUID id,String channelContent);
    void deleteChannel(UUID id);
}
