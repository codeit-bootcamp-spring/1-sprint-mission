package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository{

  private final Map<String, UserStatus> data = new ConcurrentHashMap<>();

  @Override
  public UserStatus save(UserStatus userStatus) {
    data.put(userStatus.getId(), userStatus);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(String id) {
    return data.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(id))
        .findFirst();
  }

  @Override
  public List<UserStatus> findByAllIdIn(Set<String> userIds) {
    return data.values().stream().filter(status -> userIds.contains(status.getUserId())).toList();
  }

  @Override
  public List<UserStatus> findAll() {
    return data.values().stream().toList();
  }

  @Override
  public void deleteByUserId(String id) {
    data.values().removeIf(userStatus -> userStatus.getUserId().equals(id));
  }

  @Override
  public void deleteById(String id) {
    data.remove(id);
  }
  @Override
  public void clear(){
    data.clear();
  }
}
