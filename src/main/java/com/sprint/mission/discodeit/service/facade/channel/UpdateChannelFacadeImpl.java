package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateChannelFacadeImpl implements UpdateChannelFacade{
  private final ChannelService channelService;
  private final MessageService messageService;
  private final ChannelMapper channelMapper;
  @Override
  public UpdateChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);

    List<String> userIds = channel.getParticipatingUsers();
    return channelMapper.toUpdateResponse(
        channel
    );
  }
}
