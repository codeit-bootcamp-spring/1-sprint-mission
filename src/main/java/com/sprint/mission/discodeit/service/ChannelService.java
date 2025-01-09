package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    void createChannel(String title, User owner);

    void updateTitle(Channel channel,String title);

    void displayInfo(Channel channel);

    void addMember(Channel channel, User user);
    void deleteMember(Channel channel, User user);
    List<User> getAllMemberList(Channel channel);
    User searchChannelMember(Channel channel, String userName);

    void addMessage(Channel channel, Message message);
    void deleteMessage(Channel channel, Message message);

}
