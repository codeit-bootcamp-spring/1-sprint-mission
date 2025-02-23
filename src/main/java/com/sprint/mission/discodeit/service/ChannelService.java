package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDetailDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(UUID userId);

    Channel createPublicChannel(PublicChannelDto publicChannelDto);

    ChannelDetailDto readChannel(UUID channelId);

    List<ChannelDetailDto> readAllByUserId(UUID userId);

    void updateChannel(UUID channelId, PublicChannelDto publicChannelDto);

    void addUser(UUID channelId, UUID userId);

    void deleteUser(UUID channelId, UUID userId);

    void deleteChannel(UUID channelId);
}
