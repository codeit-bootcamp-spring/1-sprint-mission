package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {
  List<UserStatus> data = new ArrayList<>();
  
  @Override
  public void save(UserStatus userStatus) {
    data.add(userStatus);
  }
  
  @Override
  public Optional<UserStatus> findById(UUID userStatusId) {
    return data.stream()
        .filter(u -> u.getId().equals(userStatusId))
        .findFirst();
  }
  
  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return data.stream()
        .filter(u -> u.getUserId().equals(userId))
        .findFirst();
  }
  
  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(data);
  }
  
  @Override
  public boolean isOnline(UUID userId) {
    if (findByUserId(userId).isPresent()) {
      return findByUserId(userId).get().isOnline();
    }
    throw new NoSuchElementException("user status not found with user id: " + userId);
  }
  
  @Override
  public void remove(UUID userId) {
    findByUserId(userId).ifPresent(data::remove);
  }
  
}
