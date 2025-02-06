package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Channel.ChannelDto;
import com.sprint.mission.discodeit.dto.Channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateRequest request);
    ChannelDto findById(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    ChannelDto update(UUID channelId, ChannelUpdateRequest request);
    void delete(UUID channelId);
}
