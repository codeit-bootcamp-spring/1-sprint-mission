package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void createChannel(String name, List<User> members);

    Channel findChannelById(UUID id);

    List<Channel> findAllChannels();

    void updateChannelName(UUID id, String newName);

    void updateMember(UUID id, List<User> members);

    void sendMessage(UUID id, Message message);

    void removeChannel(UUID id);

}
