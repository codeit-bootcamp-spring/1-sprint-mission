package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelJoinDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;

public interface ChannelService {
    Channel create(ChannelDTO channelDTO);
    Channel find(String channelId);
    Channel update(String channelId, ChannelDTO channelDTO);
    Map<User, Channel> join(ChannelJoinDTO joinDTO);
    void delete(String channelId);
    List<Channel> findAll();
}