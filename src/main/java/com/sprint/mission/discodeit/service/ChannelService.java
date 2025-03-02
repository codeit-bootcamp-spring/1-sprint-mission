package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponse createChannel(ChannelCreateRequest request);

    ChannelResponse createPublicChannel(String name, String description);

    ChannelResponse createChannel(String name, String description, ChannelType type);

    ChannelResponse getChannelById(UUID channelId);

    List<ChannelResponse> getAllChannels();

    ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest request);

    boolean deleteChannel(UUID channelId);
}