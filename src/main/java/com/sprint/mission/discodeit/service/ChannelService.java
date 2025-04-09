package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPublicChannel(PublicChannelCreateRequest request);
    ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);
    ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto create(ChannelDto channelDTO);
    ChannelDto find(UUID channelId);
    ChannelDto findById(UUID channelId);
    ChannelDto update(UUID channelId, ChannelDto channelDTO);
    Map<User, Channel> join(ChannelJoinDto joinDTO);
    void delete(UUID channelId);
    List<ChannelDto> findAll();
}