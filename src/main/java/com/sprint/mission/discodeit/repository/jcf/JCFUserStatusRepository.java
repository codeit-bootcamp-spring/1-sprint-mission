package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> data;

  public JCFUserStatusRepository() {
    data = new HashMap<>();
  }

  @Override
  public UserStatus save(UserStatus status) {
    data.put(status.getId(), status);
    return status;
  }

  @Override
  public void deleteByUserId(UUID id) {
    data.values().removeIf(user -> user.getId().equals(id));
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return data.values().stream().filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
  }
}
