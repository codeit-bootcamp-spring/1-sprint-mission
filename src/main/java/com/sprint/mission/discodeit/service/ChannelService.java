package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;

import java.util.UUID;

public interface ChannelService {
    Channel createChannel(PublicChannelCreateRequest request);
    Channel createChannel(PrivateChannelCreateRequest request);

    ChannelDto readChannelById(UUID channelId);
    List<ChannelDto> readAllByUserId(UUID userId);
    Channel updateChannelField(UUID channelId, PublicChannelUpdateRequest request);
    void deleteChannelById(UUID channelId);




}
