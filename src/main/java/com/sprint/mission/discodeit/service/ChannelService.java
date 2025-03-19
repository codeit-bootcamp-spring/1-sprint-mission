package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(ChannelCreatePublicDTO channelCreatePublicDTO);

  ChannelDto create(ChannelCreatePrivateDTO dto);

  ChannelDto find(UUID id);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID id, ChannelUpdateDTO dto);

  void delete(UUID id);
}

