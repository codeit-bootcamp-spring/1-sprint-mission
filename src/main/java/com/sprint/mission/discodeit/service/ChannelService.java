package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPrivateChannel(ChannelDTO.PrivateChannelDTO privateChannelDTO);
    Channel createPublicChannel(ChannelDTO.PublicChannelDTO publicChannelDTO);
    Channel find(UUID channelId);
    List<Channel> findAll();
    List<Channel> findAllByUserId(UUID userId);
    Channel update(UUID channelId, ChannelDTO.UpdateChannelDTO updateChannelDTO);
    void delete(UUID channelId);
}
