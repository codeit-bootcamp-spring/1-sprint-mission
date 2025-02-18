package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
  ChannelDto createChannel(ChannelDto paramChannelDto);
  ChannelDto findChannelById(UUID paramUUID);
  List<ChannelDto> findAllChannels();
  ChannelDto updateChannel(UUID paramUUID, ChannelDto paramChannelDto);
  void deleteChannel(UUID paramUUID);
}
