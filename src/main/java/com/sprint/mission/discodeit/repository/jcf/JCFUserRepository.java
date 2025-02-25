package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

  private final List<User> data = new ArrayList<>();

  @Override
  public void save(User user) {
    data.add(user);
    System.out.println("JCFUserRepository.save successful: " + data.get(data.size() - 1).getId());
  }

  @Override
  public Optional<User> findById(UUID id) {
    return data.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<User> findByName(String name) {
    return data.stream()
        .filter(u -> u.getName().equals(name))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    System.out.println("JCFUserRepository.findAll successful" + data);
    return new ArrayList<>(data);
  }

  @Override
  public void remove(UUID id) {
    findById(id).ifPresent(data::remove);
  }
}
