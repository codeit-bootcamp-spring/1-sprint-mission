package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelRequestDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPrivateChannel(PrivateChannelCreateDTO channelCreateDTO);

  Channel createPublicChannel(ChannelCreateDTO channelCreateDTO);

  ChannelRequestDTO findDTO(UUID uuid);

  List<ChannelRequestDTO> findAllByUserId(UUID userId);

  List<ChannelRequestDTO> findAllDTO();

  Channel findById(UUID id);

  List<Channel> findAll();

  Channel update(ChannelUpdateDTO channelUpdateDTO);

  void deleteChannel(UUID id);

}
