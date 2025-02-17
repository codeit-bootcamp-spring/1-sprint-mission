package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;

import java.util.List;

public interface ChannelMasterFacade {
  PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto);
  PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto);

  FindChannelResponseDto getChannelById(String channelId);
  List<FindChannelResponseDto> findAllChannelsByUserId(String userId);

  FindChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);
  void deleteChannel(String channelId);
}
