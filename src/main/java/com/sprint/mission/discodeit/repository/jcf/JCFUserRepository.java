package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")

public class JCFUserRepository implements UserRepository {

  private static volatile JCFUserRepository instance;
  private final Map<String, User> data;

  public static JCFUserRepository getInstance() {
    if (instance == null) {
      synchronized (JCFUserRepository.class) {
        if (instance == null) {
          instance = new JCFUserRepository();
        }
      }
    }
    return instance;
  }

  private JCFUserRepository() {
    this.data = new ConcurrentHashMap<>();
  }

  @Override
  public User create(User user) {
    data.put(user.getUUID(), user);
    return user;
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public User update(User user) {
    data.put(user.getUUID(), user);
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
