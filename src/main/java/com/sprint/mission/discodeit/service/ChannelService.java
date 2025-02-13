package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);
    ChannelDto createPublicChannel(PublicChannelCreateRequest request);
    ChannelDto findById(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(UUID channelId, PublicChannelUpdateRequest request);
    void delete(UUID channelId);
}
