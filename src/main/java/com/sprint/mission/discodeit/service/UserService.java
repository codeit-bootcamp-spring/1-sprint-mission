package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Gender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
  void createUser(User user);

  Optional<User> readUser(UUID id);

  List<User> readAllUsers();

  void updateUser(User user, String name, int age, Gender gender);

  void deleteUser(UUID id);
}

