package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    void setDependencies(UserService userService, MessageService messageService);
    Channel createChannel(Channel channel);
    void addParticipantToChannel(Channel channel, User user);
    Optional<Channel> readChannel(Channel channel);
    List<Channel> readAllChannels();
    Channel updateChannel(Channel existChannel, Channel updateChannel);
    boolean deleteChannel(Channel channel);
}
