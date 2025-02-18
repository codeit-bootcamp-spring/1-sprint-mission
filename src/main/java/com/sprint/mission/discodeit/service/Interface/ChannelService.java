package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelCreateRequestDto request);
    Channel createPrivateChannel(PrivateChannelCreateRequestDto request);
    Optional<ChannelResponseDto> getChannelById(UUID id);
    List<Channel> getAllChannels();
    List<ChannelResponseDto> findAllByUserId(UUID userid);
    Channel updateChannel(ChannelUpdateRequestDto request);
    void deleteChannel(UUID id);
}
