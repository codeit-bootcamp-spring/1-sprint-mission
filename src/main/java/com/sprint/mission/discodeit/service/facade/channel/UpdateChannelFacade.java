package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;

public interface UpdateChannelFacade {
  FindChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto);


}
