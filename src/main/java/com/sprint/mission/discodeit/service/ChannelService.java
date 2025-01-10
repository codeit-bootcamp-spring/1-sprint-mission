package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;

import java.util.List;

public interface ChannelService {
    MyLog<Channel> createChannel(List<User> members, String name, User creator);

    MyLog<Channel> getChannelByName(String name);

    List<Channel> getAllChannel();

    MyLog<Channel> updateChannel(String name, User newUser);

    MyLog<Channel> deleteChannel(String name);

//    void removeUserFromAllChannels(User user);
}
