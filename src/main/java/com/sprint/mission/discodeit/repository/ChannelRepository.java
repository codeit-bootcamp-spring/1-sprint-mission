package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

  Channel saveChannel(Channel channel);

  Optional<Channel> findChannelById(UUID channelId);

  List<Channel> findAllChannels();

  void removeChannel(UUID channelId);
}
