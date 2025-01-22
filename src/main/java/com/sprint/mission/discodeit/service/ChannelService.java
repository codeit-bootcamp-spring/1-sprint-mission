package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name, String topic, ChannelType type);

    Optional<Channel> getChannelDetails(UUID id);

    List<Channel> findAllChannels();

    boolean editChannel(UUID id, String name, String topic, ChannelType type);

    boolean deleteChannel(UUID id);
}

