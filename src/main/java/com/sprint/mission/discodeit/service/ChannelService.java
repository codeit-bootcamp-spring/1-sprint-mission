package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto);
    ChannelResponseDto createPublicChannel(ChannelCreateRequestDto channelCreateRequestDto);
    ChannelResponseDto createPrivateChannel(ChannelCreateRequestDto channelCreateRequestDto);
    ChannelResponseDto find(UUID channelId);
    List<ChannelResponseDto> findAll();
    ChannelResponseDto getChannelInfo(Channel channel);
    ChannelResponseDto update(ChannelUpdateRequestDto channelUpdateRequestDto);
    void delete(UUID channelId);
}
