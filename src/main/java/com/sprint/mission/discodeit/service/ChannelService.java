package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
  boolean createPublicChannel(ChannelDto channelDto);

  boolean createPrivateChannel(ChannelDto channelDto);

  ChannelDto findPublicChannel(ChannelDto channelDto);

  ChannelDto findPrivateChannelByUserId(ChannelDto channelDto);

//  void findAllByUserId(UUID userId);

  void updatePublicChannel(UpdateChannelDto updateChannelDto);

  void deleteChannel(UUID id);

  List<Channel> getAllChannels();
}