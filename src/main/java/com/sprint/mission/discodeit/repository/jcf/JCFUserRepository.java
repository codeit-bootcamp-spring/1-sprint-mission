package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

  private final Map<UUID, User> data;

  public JCFUserRepository() {
    data = new HashMap<>();
  }

  public User save(User user) {
    data.put(user.getId(), user);
    return user;
  }

  public User findById(UUID id) {
    return data.get(id);
  }

  public List<User> findAll() {
    return new ArrayList<>(data.values());
  }

  public UUID update(User user) {
    data.put(user.getId(), user);
    return user.getId();
  }

  public UUID delete(UUID id) {
    data.remove(id);
    return id;
  }

}
