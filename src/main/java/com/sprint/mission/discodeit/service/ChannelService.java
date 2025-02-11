package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    Channel create(ChannelDTO channelDTO);
    Channel find(String channelId);
    Channel update(String channelId, ChannelDTO channelDTO);
    void delete(String channelId);
    List<Channel> findAll();
}