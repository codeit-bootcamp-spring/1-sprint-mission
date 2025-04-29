package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPrivateChannel(PrivateChannelRequest channelCreateDTO);

  ChannelDto createPublicChannel(PublicChannelRequest publicChannelRequest);

  ChannelDto findDTO(UUID uuid);

  List<ChannelDto> findAllByUserId(UUID userId);

  List<ChannelDto> findAllDTO();

  Channel findById(UUID id);

  List<Channel> findAll();

  ChannelDto update(ChannelUpdateRequest channelUpdateRequest, UUID id);

  void deleteChannel(UUID id);

}
