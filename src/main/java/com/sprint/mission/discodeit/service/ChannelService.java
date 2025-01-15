package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;

public interface ChannelService {

    void createChannel(Channel channel);

    void deleteChannel(Channel channel, User admin);

    void updateChannel(Channel channel, String name, User admin);

    List<Map<String, String>> getAllChannel();

    List<String> getAllMember(Channel channel);

    boolean channelExits(Channel channel);

}
