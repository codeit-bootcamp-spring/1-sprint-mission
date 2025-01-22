package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChannelService {

  BaseChannel createChannel(BaseChannel channel);

  Optional<BaseChannel> getChannelById(String channelId);

  List<BaseChannel> getAllChannels();

  List<BaseChannel> getChannelsByCategory(String categoryId);

  void updateChannel(String channelId, ChannelUpdateDto updatedChannel);

  void deleteChannel(String channelId);

  String generateInviteCode(BaseChannel channel);

  void setPrivate(BaseChannel channel);

  void setPublic(BaseChannel channel);


}
