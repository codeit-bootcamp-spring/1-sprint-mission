package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelInfoDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(UUID userId);

    Channel createPublicChannel(ChannelDto channelDto);

    ChannelInfoDto readChannel(UUID channelId);

    List<ChannelInfoDto> readAllByUserId(UUID userId);

    void updateChannel(UUID channelId, ChannelDto channelDto);

    void addUser(UUID channelId, UUID userId);

    void deleteUser(UUID channelId, UUID userId);

    void deleteChannel(UUID channelId);
}
