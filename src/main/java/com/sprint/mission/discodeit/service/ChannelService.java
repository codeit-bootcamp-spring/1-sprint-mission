package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {

  Channel createPrivateChannel(Channel channel);
  Channel createPublicChannel(Channel channel);
  void validateUserAccess(Channel channel, String userId);
  Channel getChannelById(String channelId);

  List<Channel> findAllChannelsByUserId(String userId);

  Channel updateChannel(String channelId, ChannelUpdateDto dto);

  void deleteChannel(String channelId);

  String generateInviteCode(Channel channel);

}
