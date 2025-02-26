package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelCreateRequest publicChannelCreateRequest);
    Channel create(PrivateChannelCreateRequest privateChannelCreateRequest);
    ChannelDTO find(UUID channelId);
    List<ChannelDTO> findAllByUserId(UUID userId);
    Channel update(UUID channelId, UUID adminId, PublicChannelUpdateRequest publicChannelUpdateRequest);
    void delete(UUID channelId, UUID adminId);
    void joinPrivateChannel(UUID channelId, UUID userId);
    void leavePrivateChannel(UUID channelId, UUID userId);
}
