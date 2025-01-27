package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel);
    void addParticipantToChannel(UUID channelId, UUID userId);
    Optional<Channel> readChannel(UUID existChannelId);
    List<Channel> readAllChannels();
    Channel updateChannel(UUID existChannelId, Channel updateChannel);
    boolean deleteChannel(UUID channelId);
}
