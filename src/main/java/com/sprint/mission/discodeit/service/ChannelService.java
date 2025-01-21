package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName, User owner, Map<UUID, User> userList);

    Map<UUID, Channel> getChannelByName(String channelName);

    Channel findChannelById(UUID channelId);

    Map<UUID, Channel> getAllChannels();

    Channel updateChannel(UUID channelUUID, String channelName, User changeUser);

    void removeChannelById(UUID channelUUID);

    void addUserChannel(UUID channelUUID, User addUser);

    void kickUserChannel(UUID channelUUID, User kickUser);
}
