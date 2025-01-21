package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

  private final Map<String, User> data;

  public JCFUserRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public User create(User user) {
    data.put(user.getUUID(), user);
    return user;
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.of(data.get(id));
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
  public void clear(){
    data.clear();
  }
}
