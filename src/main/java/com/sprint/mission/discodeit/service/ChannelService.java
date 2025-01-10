package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void createChannel(Channel channel);
    Optional<Channel> channelList(UUID id);
    List<Channel> allChannelList();
    void updateChannel(Channel channel);
    void deleteChannel(UUID id);
}
