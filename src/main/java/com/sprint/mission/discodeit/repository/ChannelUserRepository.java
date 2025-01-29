package com.sprint.mission.discodeit.repository;

import java.util.Set;
import java.util.UUID;

public interface ChannelUserRepository {

  void addUser(UUID userId, UUID channelId, boolean admin);

  Set<UUID> getChannelsByUser(UUID userId);

  Set<UUID> getUsersByChannel(UUID channelId);

  Boolean isAdmin(UUID channelId, UUID userId);

  void removeUser(UUID userId);

  void removeChannel(UUID channelId);
}
