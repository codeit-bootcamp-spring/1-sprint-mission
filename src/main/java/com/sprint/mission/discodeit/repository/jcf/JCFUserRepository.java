package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
  private final List<User> data = new ArrayList<>();
  
  @Override
  public Optional<User> findUserById(UUID id) {
    return data.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public Optional<User> findUserByName(String name) {
    return data.stream()
        .filter(u -> u.getName().equals(name))
        .findFirst();
  }
  
  @Override
  public List<User> findAllUsers() {
    return data;
  }
  
  @Override
  public void save(User user) {
    data.add(user);
  }
  
  @Override
  public void remove(UUID id) {
    findUserById(id).ifPresent(data::remove);
  }
}
