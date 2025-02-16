package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createPublicChannel(ChannelRequest.CreatePublic request);
    ChannelResponse createPrivateChannel(ChannelRequest.CreatePrivate request);
    List<ChannelResponse> findAllByUserId(UUID userId);
    ChannelResponse findById(UUID id);
    Channel findByIdOrThrow(UUID id);
    ChannelResponse update(UUID id, ChannelRequest.Update request);
    void deleteById(UUID id);
}
