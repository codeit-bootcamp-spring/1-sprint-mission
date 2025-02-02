package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
  void save(Channel channel);
  Channel findById(UUID channelId);
  void delete(UUID channelId);
  Map<UUID, Channel> findAll();
}
