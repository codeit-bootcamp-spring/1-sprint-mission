package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDTO createPrivateChannel(PrivateChannelCreateDTO requestDTO);
    ChannelResponseDTO createPublicChannel(PublicChannelCreateDTO requestDTO);
    ChannelResponseDTO find(UUID channelId);
    List<ChannelResponseDTO> findAllByUserId(UUID userId);
    ChannelResponseDTO update(ChannelUpdateDTO updateDTO);
    void delete(UUID channelId);
}
