package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BaseChannel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {

  BaseChannel createChannel(BaseChannel baseChannel);
  Optional<BaseChannel> getChannelById(String id);
  List<BaseChannel> getAllChannels();
  BaseChannel updateChannel(BaseChannel channel);
  void deleteChannel(String id);
}
