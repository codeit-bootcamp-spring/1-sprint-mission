package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String name, String description);

    Channel getChannelById(UUID channelId);

    List<Channel> getAllChannels();

    Channel updateChannelName(UUID channelId, String newName);

    Channel updateDescription(UUID channelId, String newDescription);

    boolean deleteChannel(UUID channelId);

}


