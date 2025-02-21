package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {

  PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channel);
  PublicChannelResponseDto createPublicChannel(CreateChannelDto channel);

  FindChannelResponseDto getChannelById(String channelId);

  List<FindChannelResponseDto> findAllChannelsByUserId(String userId);

  List<Channel> getChannelsByCategory(String categoryId);

  void updateChannel(ChannelUpdateDto updatedChannel);

  void deleteChannel(String channelId);

  String generateInviteCode(Channel channel);

}
