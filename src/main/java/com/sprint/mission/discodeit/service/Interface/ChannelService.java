package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPublicChannel(PublicChannelCreateRequestDto request);
    ChannelDto createPrivateChannel(PrivateChannelCreateRequestDto request);
    ChannelDto getChannelById(UUID id);
    List<ChannelDto> getAllChannels();
    List<ChannelDto> findAllByUserId(UUID userid);
    Channel updateChannel(UUID channelId,ChannelUpdateRequestDto request);
    void deleteChannel(UUID id);
}
