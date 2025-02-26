package com.sprint.mission.discodeit.service.facade.channel;

import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;

import java.util.List;

public interface FindChannelFacade {
  FindChannelResponseDto findChannelById(String channelId);
  List<FindChannelResponseDto> findAllChannelsByUserId(String userId);
}
