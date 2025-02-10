package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(ChannelCreateDTO channelCreateDTO, ChannelType type);
    Channel createPublicChannel(ChannelCreateDTO channelCreateDTO, ChannelType type);

    Channel readChannel(UUID id);

    List<Channel> readAllChannel();
    Channel modifyChannel(UUID id, String name); //채널 네임 변경
    void deleteChannel (UUID id);

}
