package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
  void save(Channel channel);
  Optional<Channel> findById(UUID channelId);
  Map<UUID, Channel> findAll();
  void delete(UUID channelId);
  boolean existsById(UUID channelId);
}
