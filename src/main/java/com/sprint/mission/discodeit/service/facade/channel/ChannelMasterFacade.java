package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.*;

import java.util.List;

public interface ChannelMasterFacade {
  PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto);
  PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto);
  FindChannelResponseDto getChannelById(String channelId);
  List<FindChannelResponseDto> findAllChannelsByUserId(String userId);

  UpdateChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);
  void deleteChannel(String channelId);
}
