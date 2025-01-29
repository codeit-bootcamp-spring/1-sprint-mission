package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.relationship.ChannelUser;
import com.sprint.mission.discodeit.repository.ChannelUserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_channelUser implements ChannelUserRepository {
  private Map<UUID, Set<ChannelUser>> userIdToChannel = new HashMap<>();
  private Map<UUID, Set<ChannelUser>> channelIdToUser = new HashMap<>();

  public JCF_channelUser() {
    this.channelIdToUser = new HashMap<>();
    this.userIdToChannel = new HashMap<>();
  }

  @Override
  public void addUser(UUID userId, UUID channelId, boolean admin) {
    ChannelUser channelUser = new ChannelUser(userId, channelId, admin);
    try {
      userIdToChannel.computeIfAbsent(userId, k -> new HashSet<>()).add(channelUser);
      channelIdToUser.computeIfAbsent(channelId, k -> new HashSet<>()).add(channelUser);
    } catch (Exception e) {
      userIdToChannel.getOrDefault(userId, new HashSet<>()).remove(channelUser);
      channelIdToUser.getOrDefault(channelId, new HashSet<>()).remove(channelUser);
    }
  }

  @Override
  public Set<UUID> getChannelsByUser(UUID userId) {
    return userIdToChannel.get(userId).stream()
        .map(ChannelUser::getChannelId)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<UUID> getUsersByChannel(UUID channelId) {
    return userIdToChannel.get(channelId).stream()
        .map(ChannelUser::getUserId)
        .collect(Collectors.toSet());
  }

  @Override
  public Boolean isAdmin(UUID channelId, UUID userId) {
    return Optional.ofNullable(channelIdToUser.get(channelId))
        .flatMap(users -> users.stream()
            .filter(channelUser -> channelUser.getUserId().equals(userId))
            .map(ChannelUser::isAdmin)
            .findFirst()
        )
        .orElse(null);
  }

  @Override
  public void removeUser(UUID userId) {
    Set<ChannelUser> userChannels = userIdToChannel.get(userId);

    if (userChannels != null) {
      for (ChannelUser cu : userChannels) {
        UUID channelId = cu.getChannelId();
        channelIdToUser.getOrDefault(channelId, new HashSet<>()).remove(cu);

        if (channelIdToUser.get(channelId).isEmpty()) {
          channelIdToUser.remove(channelId);
        }
      }
    }

    userIdToChannel.remove(userId);
  }

  @Override
  public void removeChannel(UUID channelId) {
    Set<ChannelUser> channelUsers = channelIdToUser.get(channelId);

    if (channelUsers != null) {
      for (ChannelUser cu : channelUsers) {
        UUID userId = cu.getUserId();
        userIdToChannel.getOrDefault(userId, new HashSet<>()).remove(cu);

        if (channelIdToUser.get(channelId).isEmpty()) {
          channelIdToUser.remove(channelId);
        }
      }
    }

    channelIdToUser.remove(channelId);
  }


}
