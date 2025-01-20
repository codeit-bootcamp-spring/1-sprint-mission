package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String title, User owner);

    List<Channel> getAllChannelList();
    Channel searchById(UUID channelId);

    void printChannelInfo(Channel channel);
    void printChannelListInfo(List<Channel> channelList);

    void updateTitle(Channel channel, String title);
    void deleteChannel(Channel channel);

    void addMember(Channel channel, User user);
    void deleteMember(Channel channel, User user);
    List<User> getAllMemberList(Channel channel);

}
