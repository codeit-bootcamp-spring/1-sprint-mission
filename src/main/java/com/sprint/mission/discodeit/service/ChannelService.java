package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String channelName, User admin);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, UUID adminId ,String newChannelName);
    void delete(UUID channelId, UUID adminId);
    void joinChannel(UUID channelId, User user);
    void leaveChannel(UUID channelId, User user);
}
