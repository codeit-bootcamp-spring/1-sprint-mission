package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName, User owner, List<User> userList);

    Channel getChannelByName(String channelName);

    Channel findChannelById(UUID channelId);

    List<Channel> getAllChannels();

    Channel updateChannel(Channel channelToUpdate);

    void removeChannel(String channelName);

    void kickUserChannel(String channelName, User kickUser);
}
