package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel getChannelDetails(UUID id);

    CreatePrivateChannelResponse createPrivateChannel(CreatePrivateChannelRequest request);

    CreatePublicChannelResponse createPublicChannel(CreatePublicChannelRequest request);

    List<Channel> findAllChannels();

    boolean editChannel(UUID id, String name, String topic, ChannelType type);

    boolean deleteChannel(UUID id);
}

