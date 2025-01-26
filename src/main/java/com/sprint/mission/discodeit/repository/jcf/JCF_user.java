package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_user implements UserRepository {

  private final List<User> userList;
  private Set<String> nameSet = new HashSet<>();

  public JCF_user() {
    userList = new ArrayList<>();
  }

  @Override
  public void creat(User user) {
    if (!nameSet.add(user.getName())) {
      System.out.println("User name duplication!");
    } else {
      userList.add(user);
    }

  }

  @Override
  public void delete(UUID userId) {
    Optional<User> getUser = userList.stream().filter(user1 -> user1.getId().equals(userId)).findFirst();
    if (getUser.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    else {
      userList.remove(getUser.get());
    }

  }

  @Override
  public void update(UUID userId, String name) {
    userList.stream().filter(user -> user.getId().equals(userId) && nameSet.add(user.getName()))
        .forEach(user -> {nameSet.remove(user.getName());
        user.updateName(name);});
  }

  @Override
  public UUID findByName(String name) {
    Optional<User> user = userList.stream().filter(user1 -> user1.getName().equals(name)).findFirst();
    if (user.isPresent()) {
      return user.get().getId();
    }
    else {
      System.out.println("Name not found");
      return null;
    }
  }

  @Override
  public List<User> findByAll() {
    return userList.stream()
        .map(User::new)
        .collect(Collectors.toList());
  }
}
