package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import java.util.List;

public interface ChannelMasterFacade {

  ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto);

  ChannelResponseDto createPublicChannel(CreateChannelDto channelDto);

  ChannelResponseDto getChannelById(String channelId);

  //  List<ChannelResponseDto> findAllChannelsByUserId(String userId);
//  List<ChannelResponseDto> findAllChannelsByUserIdV2(String userId);
  List<ChannelResponseDto> findAllChannelsByUserIdV3(String userId);

  ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);

  void deleteChannel(String channelId);
}
