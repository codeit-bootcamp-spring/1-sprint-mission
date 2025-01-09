package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String name);

    Channel readChannel(UUID channelId);

    List<Channel> readAll();

    void updateName(UUID channelId, String name);

    void addUser(UUID channelId, UUID userId);

    void deleteUser(UUID channelId, UUID userId);

    void deleteChannel(UUID channelId);
}
