package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindChannelFacadeImpl implements FindChannelFacade {

  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;
  private final ChannelMapper channelMapper;
  private final EntityValidator validator;

  @Override
  public FindChannelResponseDto findChannelById(String channelId) {
    Channel channel = channelService.getChannelById(channelId);
    Instant lastMessageTime = messageService.getLatestMessageByChannel(channelId).getCreatedAt();
    return channelMapper.toFindChannelDto(channel, lastMessageTime);
  }

  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    List<Channel> channels = channelService.findAllChannelsByUserId(userId);
    Map<String, Instant> latestMessagesByChannel = messageService.getLatestMessageForChannels(channels);

    return channels.stream()
        .map(channel -> channelMapper.toFindChannelDto(
            channel,
            latestMessagesByChannel.getOrDefault(channel.getId(), Instant.EPOCH)
        )).toList();
  }
}
