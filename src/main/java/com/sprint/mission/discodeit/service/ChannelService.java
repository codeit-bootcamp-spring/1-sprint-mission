package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.util.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name, String description, ChannelType channelType);

    Optional<Channel> getChannelByName(String name);

    List<Channel> getAllChannel();

    Channel updateType(Channel channel, ChannelType channelType);

    Channel updateDescription(Channel channel, String description);

    void delete(Channel removeChannel);
}
