package com.sprint.mission.discodeit.service.interfacepac;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;

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
