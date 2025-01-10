package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName);

    List<Channel> getChannels();

    Optional<Channel> getChannel(UUID uuid);

    Optional<Channel> addMessageToChannel(UUID uuid, Message message);

    Optional<Channel> updateChannel(UUID uuid, String channelName);

    Optional<Channel> deleteChannel(UUID uuid);
}
