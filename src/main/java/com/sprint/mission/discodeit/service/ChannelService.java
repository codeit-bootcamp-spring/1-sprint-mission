package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPublicChannel(String channel, String description);

    Channel createPrivateChannel(String name, String description);

    Channel find(UUID id);

    List<Channel> findAll();


    Channel update(UUID channelId, String newName, String newDescription);


    void delete(UUID channelId);
}