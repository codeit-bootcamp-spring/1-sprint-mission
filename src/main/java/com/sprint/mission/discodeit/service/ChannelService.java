package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel);
    void addParticipantToChannel(Channel channel, User user);
    Optional<Channel> readChannel(UUID channelId);
    List<Channel> readAllChannels();
    Channel updateChannel(UUID channelId, Channel updateChannel);
    boolean deleteChannel(Channel channel);
}
