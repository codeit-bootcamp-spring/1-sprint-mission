package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")

public class JCFUserRepository implements UserRepository{

  private final Map<String, User> data = new ConcurrentHashMap<>();

  @Override
  public User create(User user) {
    data.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return data.values().stream().filter(u -> u.getUsername().equals(username)).findAny();
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public User update(User user) {
    data.put(user.getId(), user);
    return user;
  }

  @Override
  public void delete(String userId) {
    data.remove(userId);
  }

  @Override
  public void clear() {
    data.clear();
  }
}
