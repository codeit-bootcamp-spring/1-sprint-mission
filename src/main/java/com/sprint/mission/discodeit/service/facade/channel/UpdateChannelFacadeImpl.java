package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateChannelFacadeImpl implements UpdateChannelFacade{
  private final ChannelService channelService;
  private final ChannelMapper channelMapper;
  @Override
  public UpdateChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);

    return channelMapper.toUpdateResponse(
        channel
    );
  }
}
