package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponse createPublicChannel(ChannelRequest.CreatePublic request);

    ChannelResponse createPrivateChannel(ChannelRequest.CreatePrivate request);

    List<ChannelResponse> findAllByUserId(UUID userId);

    ChannelResponse findById(UUID id);

    ChannelResponse update(UUID id, ChannelRequest.Update request);

    List<UUID> findParticipantIdsById(UUID id);

    void deleteById(UUID id);
}
