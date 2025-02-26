package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelResponseDto;

public interface CreateChannelFacade {

  PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto);
  PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto);
}
