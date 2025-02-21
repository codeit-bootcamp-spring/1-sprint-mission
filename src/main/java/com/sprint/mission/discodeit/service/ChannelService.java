package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPrivateChannel(ChannelCreateDTO channelCreateDTO);
    Channel createPublicChannel(ChannelCreateDTO channelCreateDTO);

    ChannelFindDTO findDTO(UUID uuid);
    List<ChannelFindDTO> findAllByUserId(UUID userId);
    List<ChannelFindDTO> findAllDTO();

    Channel readChannel(UUID id);
    List<Channel> readAllChannel();

    Channel update(ChannelUpdateDTO channelUpdateDTO);
    void deleteChannel (UUID id);

}
