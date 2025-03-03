package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> store;

  public JCFUserStatusRepository() {
    store = new HashMap<>();
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    store.put(userStatus.getId(), userStatus);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return store.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public boolean existsById(UUID id) {
    return store.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    store.remove(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    store.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
  }

}
