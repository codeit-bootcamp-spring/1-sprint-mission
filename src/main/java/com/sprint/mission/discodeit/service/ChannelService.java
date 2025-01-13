package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(UUID id, String userName, String channel);

    Channel getChannel(UUID id);

    List<Channel> allChannel();

    List<Channel> searchChannelsByName(String keyword);

    void updateChannel(UUID id, String newChannelName);

    void deleteChannel(UUID id);

    void deleteAllChannel();

    void deleteChannelsByUser(String userName);

}
