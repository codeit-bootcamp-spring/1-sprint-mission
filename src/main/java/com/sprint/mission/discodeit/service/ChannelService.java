package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;

public interface ChannelService {

  Channel createPrivateChannel(Channel channel);
  Channel createPublicChannel(Channel channel);
  void validateUserAccess(Channel channel, String userId);
  Channel getChannelById(String channelId);

  List<Channel> findAllChannelsByUserId(String userId);

  Channel updateChannel(String channelId, String channelName, int maxNumberOfPeople);

  void deleteChannel(String channelId);

  String generateInviteCode(Channel channel);

}
