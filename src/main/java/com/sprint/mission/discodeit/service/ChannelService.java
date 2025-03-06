package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    ChannelDto create(ChannelDto channelDTO);
    ChannelDto find(UUID channelId);
    ChannelDto update(UUID channelId, ChannelDto channelDTO);
    Map<User, Channel> join(ChannelJoinDto joinDTO);
    void delete(UUID channelId);
    List<ChannelDto> findAll();
}