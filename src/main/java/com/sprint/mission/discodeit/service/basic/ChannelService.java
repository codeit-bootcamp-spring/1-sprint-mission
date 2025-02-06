package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channelService.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(ChannelCreateRequest request);
    Channel createPrivateChannel(PrivateChannelCreateRequest request);

    ChannelDTO find(UUID channelId);
    List<ChannelDTO> findAllByUserId(UUID userId);
    ChannelDTO update(ChannelUpdateRequest request);
    void delete(UUID channelId);

    Instant findLastMessageAtByChannelId(UUID channelId);
}
