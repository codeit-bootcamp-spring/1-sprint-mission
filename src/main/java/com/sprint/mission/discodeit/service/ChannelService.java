package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name, UUID ownerId);

    Channel getChannelById(UUID channelId);
    List<Channel> getAllChannels();

    List<User> getChannelUsers(UUID channelId);

    void updateChannelName(UUID channelId, String newName);

    void deleteChannel(UUID channelId);

    void addUser(UUID channelId, UUID userId);
    void removeUser(UUID channelId, UUID userId);
}
