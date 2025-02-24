package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.sprint.mission.discodeit.constant.UserConstant.NO_MATCHING_USER;


@Slf4j
@Component
@RequiredArgsConstructor
public class CreateChannelFacadeImpl implements CreateChannelFacade{

  private final ChannelService channelService;
  private final ReadStatusService readStatusService;
  private final ChannelMapper channelMapper;
  private final EntityValidator validator;

  @Override
  public PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {
    channelDto.participantIds().forEach(
        id -> validator.findOrThrow(User.class, id, new NotFoundException(NO_MATCHING_USER))
    );

    Channel channel = channelService.createPrivateChannel(channelMapper.toEntity(channelDto));

    readStatusService.createMultipleReadStatus(channelDto.participantIds(), channel.getId());

    return channelMapper.toPrivateDto(channel);
  }

  @Override
  public PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    Channel channel = channelService.createPublicChannel(channelMapper.toEntity(channelDto));
    return channelMapper.toPublicDto(channel);
  }
}
