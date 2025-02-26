package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFUserReposiroty implements UserRepository {

  private final Map<UUID, User> store;

  public JCFUserReposiroty() {
    store = new HashMap<>();
  }

  @Override
  public User save(User user) {
    store.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return store.values().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
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
  public boolean existsByUsername(String username) {
    return store.values().stream()
        .anyMatch(user -> user.getUsername().equals(username));
  }

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return store.values().stream()
        .anyMatch(user -> user.getPhoneNumber().equals(phoneNumber));
  }

  @Override
  public boolean existsByEmail(String email) {
    return store.values().stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }
}
