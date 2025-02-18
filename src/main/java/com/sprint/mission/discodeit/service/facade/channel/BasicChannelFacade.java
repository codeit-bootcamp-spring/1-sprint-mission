package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.facade.channel.ChannelMasterFacade;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BasicChannelFacade implements ChannelMasterFacade {

  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;
  private final ChannelMapper channelMapper;
  private final EntityValidator validator;

  private final CreateChannelFacade createChannelFacade;
  private final FindChannelFacade findChannelFacade;
  private final UpdateChannelFacade updateChannelFacade;
  @Override
  public PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {
    return createChannelFacade.createPrivateChannel(channelDto);
  }

  @Override
  public PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    return createChannelFacade.createPublicChannel(channelDto);
  }

  @Override
  public FindChannelResponseDto getChannelById(String channelId) {
    return findChannelFacade.findChannelById(channelId);
  }

  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    return findChannelFacade.findAllChannelsByUserId(userId);
  }

  @Override
  public FindChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    return updateChannelFacade.updateChannel(channelId, channelUpdateDto);

  }

  @Override
  public void deleteChannel(String channelId) {
    channelService.deleteChannel(channelId);
    readStatusService.deleteByChannel(channelId);
    //TODO : 한번에 삭제 메서드 생성
    messageService.getMessagesByChannel(channelId).stream()
        .forEach(message -> messageService.deleteMessage(message.getUUID()));
  }
}
