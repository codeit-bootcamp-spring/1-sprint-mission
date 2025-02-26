package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMasterFacadeImpl implements ChannelMasterFacade {

  private final CreateChannelFacade createChannelFacade;
  private final FindChannelFacade findChannelFacade;
  private final UpdateChannelFacade updateChannelFacade;
  private final DeleteChannelFacade deleteChannelFacade;
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
  public UpdateChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {
    return updateChannelFacade.updateChannel(channelId, channelUpdateDto);

  }

  @Override
  public void deleteChannel(String channelId) {
    deleteChannelFacade.deleteChannel(channelId);
  }
}
