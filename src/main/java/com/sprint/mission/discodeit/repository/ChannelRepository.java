package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
  Channel save(Channel paramChannel);

  Optional<Channel> findById(UUID paramUUID);

  List<Channel> findAll();

  boolean existsById(UUID paramUUID);

  void deleteById(UUID paramUUID);
}
