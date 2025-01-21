package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(ChannelDto channelDto);

    Channel readChannel(UUID channelId);

    List<Channel> readAll();

    void updateChannel(UUID channelId, ChannelDto channelDto);

    void addUser(UUID channelId, UUID userId);

    void deleteUser(UUID channelId, UUID userId);

    void deleteChannel(UUID channelId);
}
