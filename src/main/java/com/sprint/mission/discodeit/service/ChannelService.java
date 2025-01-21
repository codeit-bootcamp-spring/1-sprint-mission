package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    void createChannel(Channel channel);

    Channel readChannel(String id);

    void updateChannel(Channel channel);

    void deleteChannel(String id);

    void addMember(String channelid, User member);

    void removeMember(String channelid, User member);
}
