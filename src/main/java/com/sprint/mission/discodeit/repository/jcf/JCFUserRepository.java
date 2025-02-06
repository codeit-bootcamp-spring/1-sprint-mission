package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;


import java.util.*;

public class JCFUserRepository implements UserRepository {
  private final List<User> data;

  public JCFUserRepository() {
    this.data = new ArrayList<>();
  }

  public void save(User user){
    this.data.add(user);
  }

  public Optional<User> findById(UUID id) {
    return data.stream()
      .filter(user -> user.getId().equals(id))
      .findFirst();
  }

  public List<User> findAll() {
    return data;
  }

  public void updateOne(User user, String name, int age, Gender gender) {
    if (user != null) {
      user.update(name, age, gender);
    } else {
      // 예외 처리 또는 로깅
      System.out.println("User is null!");
    }
  }


  public void deleteOne(UUID id) {
    data.removeIf(user -> user.getId().equals(id));
  }
}