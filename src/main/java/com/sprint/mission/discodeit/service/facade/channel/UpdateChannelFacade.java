package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;

public interface UpdateChannelFacade {
  FindChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);


}
