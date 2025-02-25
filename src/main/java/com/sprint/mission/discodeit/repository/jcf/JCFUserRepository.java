package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

  private final Map<UUID, User> data;

  public JCFUserRepository() {
    data = new HashMap<>();
  }


  @Override
  public User save(User user) {
    this.data.put(user.getId(), user);
    return user;
  }


  @Override
  public Optional<User> getUserById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<User> getAllUsers() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }


  @Override
  public void deleteUser(UUID id) {
    data.remove(id);
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.data.containsKey(email);
  }

  @Override
  public boolean existsByName(String name) {
    return this.data.containsKey(name);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return this.getAllUsers().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }
}
